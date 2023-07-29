package com.example.shopify.orders.presentation.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopify.NavGraphDirections
import com.example.shopify.databinding.FragmentOrdersBinding
import com.example.shopify.orders.domain.model.OrderModel
import com.example.shopify.utils.connectivity.ConnectivityObserver
import com.example.shopify.utils.ui.visibleIf
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class OrdersFragment(
    private val connectivityObserver: ConnectivityObserver,
    private val firebaseAuth: FirebaseAuth
) : Fragment() {

    lateinit var binding: FragmentOrdersBinding
    private lateinit var navController: NavController
    private lateinit var ordersAdapter: OrdersAdapter
    private var currency = "EGP"
    private var exchangeRate: Double = 1.0
    private val viewModel: OrdersViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentOrdersBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        binding.guestView.navToAuthBtn.setOnClickListener {
            navController.navigate(NavGraphDirections.actionToAuthenticationGraph())
        }
        binding.backImageView.setOnClickListener {
            navController.navigateUp()
        }
        ordersAdapter = OrdersAdapter {
            goToOrderDetails(it)
        }
        setOrdersRecycler()
        checkConnection()
        stateObserve()
    }

    private fun checkConnection() {
//        val connectivitySnackBar = Snackbar.make(
//            binding.root, getString(com.firebase.ui.auth.R.string.fui_no_internet),
//            Snackbar.LENGTH_INDEFINITE
//        )
        lifecycleScope.launch {
            connectivityObserver.observe().collectLatest { connectionStatus ->
                delay(200)
                when (connectionStatus) {
                    ConnectivityObserver.Status.Available -> {
                        var email = ""
                        firebaseAuth.currentUser?.let{
                            email = it.email?:""
                        }

                        viewModel.readCurrencyFactorFromDataStore()
                        viewModel.getCustomerOrders(email)
                    }

                    else -> {
//                        connectivitySnackBar.show()
                    }
                }
            }
        }
    }

    private fun stateObserve() {
        lifecycleScope.launch(Dispatchers.IO) {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {

                viewModel.ordersState.collectLatest {
                    delay(200)
                    withContext(Dispatchers.Main) {
                        if (it.orders.isNotEmpty()) {
                            ordersAdapter.submitList(it.orders)
                        }
                        if (it.loading == true) {
                            binding.progressBar.visibility = View.VISIBLE
                        } else {
                            binding.progressBar.visibility = View.GONE
                        }
                        if (it.currency != "EGP") {
                            exchangeRate = it.exchangeRate
                            currency = it.currency
                            ordersAdapter.exchangeRate = it.exchangeRate
                            ordersAdapter.currency = it.currency
                        }
                        binding.guestView.root visibleIf it.guestMode
                    }
                }
            }
        }
    }

    private fun setOrdersRecycler() {
        val ordersLayoutManager = LinearLayoutManager(requireContext())
        ordersLayoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.ordersRv.apply {
            adapter = ordersAdapter
            layoutManager = ordersLayoutManager
        }
    }

    private fun goToOrderDetails(orderModel: OrderModel) {
        navController.navigate(
            OrdersFragmentDirections.actionOrdersFragmentToOrderDetailsFragment(
                orderModel, currency, exchangeRate.toString()
            )
        )
    }
}