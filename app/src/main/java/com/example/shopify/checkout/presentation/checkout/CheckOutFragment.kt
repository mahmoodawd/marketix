package com.example.shopify.checkout.presentation.checkout

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.shopify.R
import com.example.shopify.checkout.data.dto.post.LineItem
import com.example.shopify.checkout.data.dto.post.Order
import com.example.shopify.checkout.data.dto.post.PostOrder
import com.example.shopify.checkout.domain.model.CartItems
import com.example.shopify.data.dto.PropertiesItem
import com.example.shopify.databinding.AddressBottomSheetBinding
import com.example.shopify.databinding.CodeBottomSheetBinding
import com.example.shopify.databinding.FragmentCheckOutBinding
import com.example.shopify.home.domain.model.discountcode.DiscountCodeModel
import com.example.shopify.settings.domain.model.CurrencyModel
import com.example.shopify.utils.snackBarObserver
import com.paypal.checkout.approve.OnApprove
import com.paypal.checkout.cancel.OnCancel
import com.paypal.checkout.createorder.CreateOrder
import com.paypal.checkout.createorder.CurrencyCode
import com.paypal.checkout.createorder.OrderIntent
import com.paypal.checkout.createorder.UserAction
import com.paypal.checkout.order.Amount
import com.paypal.checkout.order.AppContext
import com.paypal.checkout.order.OrderRequest
import com.paypal.checkout.order.PurchaseUnit
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber


@AndroidEntryPoint
class CheckOutFragment : Fragment() {


    private lateinit var binding: FragmentCheckOutBinding


    private lateinit var navController: NavController

    private val viewModel: CheckOutViewModel by viewModels()

    private var cartItems: CartItems? = null

    private val addressesRecyclerAdapter by lazy {
        AddressesRecyclerAdapter { address ->
            viewModel.onEvent(CheckOutIntent.ChooseAddress(address))
            addressDialog.dismiss()
        }
    }

    private val addressDialog by lazy {
        Dialog(requireContext())
    }

    private val discountDialog by lazy {
        Dialog(requireContext())
    }

    private val discountCodesRecyclerAdapter by lazy {
        DiscountCodesRecyclerAdapter { code ->
            viewModel.onEvent(CheckOutIntent.ChooseDiscountCode(code))
            viewModel.onEvent(CheckOutIntent.ValidateDiscountCode)
            discountDialog.dismiss()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCheckOutBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()
        stateObserver()
        requireActivity().snackBarObserver(viewModel.snackBarFlow)
        binding.editEmailIconImageView.setOnClickListener {
            navController.navigate(CheckOutFragmentDirections.actionCheckOutFragmentToEmailDialogFragment())
        }

        binding.editPhoneIconImageView.setOnClickListener {
            navController.navigate(CheckOutFragmentDirections.actionCheckOutFragmentToPhoneDialogFragment())
        }

        binding.checkOutButton.setOnClickListener {
            createOrder(cartItems as CartItems)
//            navController.navigate(CheckOutFragmentDirections.actionCheckOutFragmentToDiscountFragment())
        }


        binding.addressDownImageView.setOnClickListener {
            showAddressSheet()
        }

        binding.discountCode.setOnClickListener {
            showDiscountCodesSheet()
        }

        binding.mapImageView.setOnClickListener {
            with(viewModel.state.value.deliveryAddress!!) {
                openInMap(latitude, longitude, address)
            }
        }
        dialogBackObserver()
        paypalSetup()
        getSubTotal()
        checkOutCompletedObserver()

    }


    private fun openInMap(latitude: Double, longitude: Double, address: String?) {
        val intent = Intent(Intent.ACTION_CHOOSER).apply {
            data = Uri.parse("geo:$latitude,$longitude?q=" + Uri.parse(address))
        }
        startActivity(intent)

    }

    private fun getSubTotal() {
        val subTotal = arguments?.getString(getString(R.string.subtotalType))
        subTotal?.let {
            viewModel.onEvent(CheckOutIntent.UserSubTotal(subTotal))
        }


        cartItems = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(getString(R.string.cartItems), CartItems::class.java)
        } else {
            arguments?.getParcelable(getString(R.string.cartItems))
        }
        cartItems?.let {
            viewModel.onEvent(CheckOutIntent.NewCartItems(cartItems as CartItems))
        }
    }


    @SuppressLint("SetTextI18n")
    private fun stateObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.state.collectLatest { state ->

                    discountCodesRecyclerAdapter.submitList(state.discountCodes)
                    addressesRecyclerAdapter.submitList(state.addresses)
                    binding.emailValueTextView.text = state.email
                    binding.phoneValueTextView.text = state.phone
                    binding.subtotalValueTextView.text = "${state.subtotal} ${state.currency}"
                    binding.totalCostValueTextView.text = "${state.totalCost} ${state.currency}"
                    binding.discountCodeValueTextView.text =
                        "${state.discountValue} ${state.currency}"
                    state.deliveryAddress?.let {
                        binding.addressValueTextView.text = state.deliveryAddress.address
                        binding.addressSection.visibility = View.VISIBLE
                    } ?: kotlin.run {
                        binding.addressSection.visibility = View.GONE
                    }
                }
            }
        }
    }


    private fun dialogBackObserver() {
        val backStackEntry: NavBackStackEntry = navController.currentBackStackEntry!!

        lifecycleScope.launch {
            val phone = backStackEntry.savedStateHandle.getStateFlow(
                getString(R.string.phoneType),
                ""
            )

            phone.collectLatest {
                if (it.isNotEmpty()) {
                    viewModel.onEvent(CheckOutIntent.UserEditPhone(it))
                }
            }
        }
        lifecycleScope.launch {

            val email = backStackEntry.savedStateHandle.getStateFlow(
                getString(R.string.emailType),
                ""
            )

            email.collectLatest {
                if (it.isNotEmpty()) {
                    viewModel.onEvent(CheckOutIntent.UserEditEmail(it))
                }
            }
        }
    }

    private fun showDiscountCodesSheet() {
        val bottomSheetBinding = CodeBottomSheetBinding.inflate(layoutInflater)
        discountDialog.apply {
            setContentView(bottomSheetBinding.root)

            window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setUpRecyclerView(bottomSheetBinding.codesRecycler, discountCodesRecyclerAdapter)

            bottomSheetBinding.done.setOnClickListener {
                dismiss()
            }

            window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
            window?.attributes?.windowAnimations = R.style.DialogAnimation
            window?.setGravity(Gravity.BOTTOM)

        }.show()
    }

    private fun showAddressSheet() {
        val bottomSheetBinding = AddressBottomSheetBinding.inflate(layoutInflater)

        addressDialog.apply {
            setContentView(bottomSheetBinding.root)
            window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setUpRecyclerView(bottomSheetBinding.addressesRecycler, addressesRecyclerAdapter)

            bottomSheetBinding.done.setOnClickListener {
                dismiss()
            }

            window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
            window?.attributes?.windowAnimations = R.style.DialogAnimation
            window?.setGravity(Gravity.BOTTOM)
        }.show()
    }


    private fun setUpRecyclerView(recyclerView: RecyclerView, listAdapter: ListAdapter<*, *>) {
        val linearLayoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

        recyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = listAdapter
        }
    }


    private fun paypalSetup() {

        binding.paymentButtonContainer.setup(
            createOrder =
            CreateOrder { createOrderActions ->

                if (viewModel.state.value.deliveryAddress == null) {
                    viewModel.onEvent(CheckOutIntent.EmitMessage(R.string.please_choose_address))
                    return@CreateOrder
                }

                if (viewModel.state.value.email.isEmpty()) {
                    viewModel.onEvent(CheckOutIntent.EmitMessage(R.string.please_write_your_email))
                    return@CreateOrder
                }

                if (viewModel.state.value.phone.isEmpty()) {
                    viewModel.onEvent(CheckOutIntent.EmitMessage(R.string.please_write_your_phone_number))
                    return@CreateOrder
                }


                val order =
                    OrderRequest(
                        intent = OrderIntent.CAPTURE,
                        appContext = AppContext(userAction = UserAction.PAY_NOW),
                        purchaseUnitList =
                        listOf(
                            PurchaseUnit(
                                amount =
                                Amount(currencyCode = CurrencyCode.USD, value = viewModel.state.value.totalCost.toString())
                            )
                        )
                    )
                createOrderActions.create(order)
            },
            onApprove =
            OnApprove { approval ->
                createOrder(cartItems as CartItems)
            },

            onCancel = OnCancel {
                Log.d("paypalResult", "Buyer canceled the PayPal experience.")
            }
        )
    }

    private fun createOrder(cartItems: CartItems) {
        val lineItems = mutableListOf<LineItem>()
        val draftOrdersIds = mutableListOf<Long>()
        val carItems = viewModel.state.value.cartItems
        val email = viewModel.state.value.email
        Timber.e(carItems.toString())
        for (item in carItems) {
            lineItems.add(
                LineItem(
                    item.quantity.toInt(), item.variantId.toLong(), listOf(
                        PropertiesItem(value = item.imageUrl)
                    )
                )
            )
            draftOrdersIds.add(item.itemId)
        }
        Timber.e(lineItems.toString())
        viewModel.onEvent(
            CheckOutIntent.CreateOrder(
                PostOrder(
                    Order(
                        email,
                        lineItems,
                    )
                ), draftOrdersIds
            )
        )
    }


    private fun checkOutCompletedObserver(){
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.checkOutCompletedFlow.collectLatest {
                    navController.popBackStack(R.id.homeFragment, false)
                }
            }
        }
    }
}