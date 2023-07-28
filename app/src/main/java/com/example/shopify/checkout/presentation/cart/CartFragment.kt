package com.example.shopify.checkout.presentation.cart

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopify.NavGraphDirections
import com.example.shopify.checkout.domain.model.CartItems
import com.example.shopify.databinding.FragmentCartBinding
import com.example.shopify.utils.snackBarObserver
import com.example.shopify.utils.ui.visibleIf
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class CartFragment : Fragment() {


    private lateinit var binding: FragmentCartBinding

    private lateinit var navController: NavController

    private val viewModel: CartViewModel by viewModels()


    private val cartRecyclerAdapter by lazy {
        CartRecyclerAdapter(onPlusClickAction = { updatedId, quantity, position ->
            viewModel.onEvent(
                CartIntent.UpdateCartItem(
                    updatedId.toString(),
                    (quantity.toInt() + 1).toString(),
                    position
                )
            )
        }, onMinusClickAction = { updatedId, quantity, position ->

            if (quantity.toInt() >= 2) {
                viewModel.onEvent(
                    CartIntent.UpdateCartItem(
                        updatedId.toString(),
                        (quantity.toInt() - 1).toString(),
                        position
                    )
                )
            }

        }) { deletedItemId, position ->
            viewModel.onEvent(CartIntent.DeleteCartItem(deletedItemId.toString(), position))
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        requireActivity().snackBarObserver(viewModel.snackBarFlow)
        getAllCartItems()
        setUpCartRecyclerView()
        stateObserver()
        binding.checkOutButton.setOnClickListener {
            navController.navigate(
                CartFragmentDirections.actionCartFragmentToCheckOutFragment(
                    viewModel.state.value.cartTotalCost.toString(),
                    CartItems(viewModel.state.value.cartItems)
                )
            )
        }


        binding.guestView.navToAuthBtn.setOnClickListener {
            navController.navigate(NavGraphDirections.actionToAuthenticationGraph())
        }


        binding.backImageView.setOnClickListener {
            navController.popBackStack()
        }

    }

    private fun getAllCartItems() {
        if (!viewModel.state.value.userIsGuest) {
           Log.d("userIsGuest","user")
            viewModel.onEvent(CartIntent.GetAllCartItems)
        }
    }


    private fun setUpCartRecyclerView() {
        val linearLayoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.cartRecyclerView.layoutManager = linearLayoutManager
        binding.cartRecyclerView.adapter = cartRecyclerAdapter
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun stateObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.state.collectLatest { state ->
                    cartRecyclerAdapter.submitList(state.cartItems)
                    cartRecyclerAdapter.notifyDataSetChanged()
                    binding.progressBar visibleIf state.loading
                    binding.totalCostValueTextView visibleIf (state.cartItems.isNotEmpty() && !state.loading)
                    binding.totalCostTitleTextView visibleIf (state.cartItems.isNotEmpty() && !state.loading)
                    binding.checkOutButton visibleIf (state.cartItems.isNotEmpty() && !state.loading)
                    binding.cartRecyclerView visibleIf (state.cartItems.isNotEmpty() && !state.loading)
                    binding.cartEmptyImageView visibleIf (state.cartItems.isEmpty() && !state.loading&& !state.userIsGuest)
                    binding.emptyCartTextView visibleIf (state.cartItems.isEmpty() && !state.loading && !state.userIsGuest)
                    binding.totalCostValueTextView.text = "${state.cartTotalCost} ${state.currency}"
                    binding.guestView.root visibleIf state.userIsGuest

                }
            }
        }
    }
}