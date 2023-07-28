package com.example.shopify.checkout.presentation.checkout

import android.annotation.SuppressLint
import android.app.AlertDialog
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
import android.widget.AdapterView
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
import com.example.shopify.NavGraphDirections
import com.example.shopify.R
import com.example.shopify.checkout.data.dto.post.DiscountCode
import com.example.shopify.checkout.data.dto.post.LineItem
import com.example.shopify.checkout.data.dto.post.Order
import com.example.shopify.checkout.data.dto.post.PostOrder
import com.example.shopify.checkout.domain.model.CartItems
import com.example.shopify.data.dto.PropertiesItem
import com.example.shopify.databinding.AddressBottomSheetBinding
import com.example.shopify.databinding.CodeBottomSheetBinding
import com.example.shopify.databinding.FragmentCheckOutBinding
import com.example.shopify.databinding.SuccessDialogBinding
import com.example.shopify.home.domain.model.discountcode.DiscountCodeModel
import com.example.shopify.utils.snackBarObserver
import com.example.shopify.utils.ui.visibleIf
import com.google.android.material.snackbar.Snackbar
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
import kotlin.math.absoluteValue


@AndroidEntryPoint
class CheckOutFragment : Fragment() {


    private lateinit var binding: FragmentCheckOutBinding


    private lateinit var navController: NavController

    private val viewModel: CheckOutViewModel by viewModels()

    private var mCartItems: CartItems? = null

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
            createOrder(mCartItems as CartItems)
        }


        binding.addressDownImageView.setOnClickListener {
            showAddressSheet()
        }


        binding.backImageView.setOnClickListener {
            navController.popBackStack()
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
        spinnerSetup(null, null)
        codeSelector()
    }

    private fun codeSelector() {
        binding.codesSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    viewModel.onEvent(CheckOutIntent.ChooseDiscountCode(position))
                    viewModel.onEvent(CheckOutIntent.ValidateDiscountCode)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
    }

    private fun spinnerSetup(
        arraySpinner: List<DiscountCodeModel>?,
        selectedItem: DiscountCodeModel?
    ) {


        val adapter = ArrayAdapter(requireContext(),
            android.R.layout.simple_spinner_item, arraySpinner?.map { it.code } ?: mutableListOf())
        binding.codesSpinner.adapter = adapter

        binding.codesSpinner.setSelection(0)
//
    }


    private fun openInMap(latitude: Double, longitude: Double, address: String?) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            Log.d("latLong", "$latitude $longitude")
            data = Uri.parse("geo:$latitude,$longitude?z=10f")
        }
        startActivity(intent)

    }

    private fun getSubTotal() {
        val subTotal = arguments?.getString(getString(R.string.subtotalType))
        subTotal?.let {
            viewModel.onEvent(CheckOutIntent.UserSubTotal(subTotal))
        }


        mCartItems = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(getString(R.string.cartItems), CartItems::class.java)
        } else {
            arguments?.getParcelable(getString(R.string.cartItems))
        }
        mCartItems?.let {
            viewModel.onEvent(CheckOutIntent.NewCartItems(mCartItems as CartItems))
        }
    }


    @SuppressLint("SetTextI18n")
    private fun stateObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.state.collectLatest { state ->
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
                    spinnerSetup(state.discountCodes, state.discountCode)
                    binding.discountInputLayout.editText!!.setText(state.discountCode?.code)
                    binding.codesSpinner visibleIf state.discountCodes.isNotEmpty()
                    binding.discountInputLayout visibleIf state.discountCodes.isNotEmpty()
                    binding.dropDownCodesImageView visibleIf state.discountCodes.isNotEmpty()
                    binding.progressBar visibleIf state.loading

                    if (state.error.isNotEmpty()){
                        Snackbar.make(
                            binding.root, state.error,
                            Snackbar.LENGTH_SHORT
                        ).show()
                        viewModel.resetError()
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
            //   setUpRecyclerView(bottomSheetBinding.codesRecycler, discountCodesRecyclerAdapter)

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
                                Amount(
                                    currencyCode = CurrencyCode.USD,
                                    value = viewModel.state.value.totalCost.toString()
                                )
                            )
                        )
                    )
                createOrderActions.create(order)
            },
            onApprove =
            OnApprove { approval ->
                createOrder(mCartItems as CartItems)
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
        val priceRule = viewModel.state.value.priceRule
        val discountCode = viewModel.state.value.discountCodes.firstOrNull()
        Timber.e(viewModel.state.value.priceRule?.discount ?: "no value")
        Timber.e(viewModel.state.value.priceRule?.type ?: "no value")
        val amountCheck = priceRule?.discount?.toDouble()?.absoluteValue.toString()
        val amount = if (amountCheck == "null") "" else amountCheck

        val discountCodes =
            mutableListOf(DiscountCode(discountCode?.code ?: "", amount, priceRule?.type ?: ""))
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
                        discountCodes
                    )
                ), draftOrdersIds
            )
        )
    }


    private fun checkOutCompletedObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.checkOutCompletedFlow.collectLatest {
                    showSuccessDialog()
                }
            }
        }
    }


    private fun showSuccessDialog() {
        val successDialog = SuccessDialogBinding.inflate(LayoutInflater.from(requireContext()))

        AlertDialog.Builder(requireContext()).create().apply {
            setOnDismissListener {
                navController.popBackStack(R.id.home_graph, true)
                navController.navigate(NavGraphDirections.actionToHomeGraph())
            }
            setView(successDialog.root)
            successDialog.dismissBtn.setOnClickListener {
                navController.popBackStack(R.id.home_graph, true)
                navController.navigate(NavGraphDirections.actionToHomeGraph())
                dismiss()
            }
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window?.setGravity(Gravity.CENTER)
            show()
        }
    }
}