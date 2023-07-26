package com.example.shopify.orders.presentation.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopify.data.dto.PropertiesItem
import com.example.shopify.databinding.FragmentOrdersBinding
import com.example.shopify.orders.data.dto.post.Order
import com.example.shopify.orders.data.dto.post.PostOrder
import com.example.shopify.orders.domain.model.OrderModel
import com.example.shopify.utils.connectivity.ConnectivityObserver
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
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
        lifecycleScope.launch {
            connectivityObserver.observe().collectLatest {
                when (it) {
                    ConnectivityObserver.Status.Available -> {
                        viewModel.getCustomerOrders(firebaseAuth.currentUser?.email as String)
                        viewModel.createOrder(
                            PostOrder(
                                Order(
                                    "mohamedadel2323m@gmail.com",
                                    listOf(
                                        com.example.shopify.orders.data.dto.post.LineItem(1,45736853176599,
                                            listOf(PropertiesItem("image_url","https://cdn.shopify.com/s/files/1/0790/0712/1687/products/85cc58608bf138a50036bcfe86a3a362.jpg?v=1689452647"))
                                        )
                                    )
                                )
                            )
                        )

                    }

                    else -> {
                        Toast.makeText(requireContext(), "No Connection", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun stateObserve() {
        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.ordersState.collectLatest {
                withContext(Dispatchers.Main) {
                    if (it.orders.isNotEmpty()) {
                        ordersAdapter.submitList(it.orders)
                    }
                    if (it.loading == true) {
                        binding.progressBar.visibility = View.VISIBLE
                    } else {
                        binding.progressBar.visibility = View.GONE
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
                orderModel
            )
        )
    }
}