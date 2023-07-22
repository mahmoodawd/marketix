package com.example.shopify.orders.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopify.databinding.FragmentOrdersBinding
import com.example.shopify.utils.connectivity.ConnectivityObserver
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class OrdersFragment(
    private val connectivityObserver: ConnectivityObserver,
    private val firebaseAuth: FirebaseAuth
) : Fragment() {

    lateinit var binding: FragmentOrdersBinding
    private lateinit var navController: NavController
    lateinit var ordersAdapter: OrdersAdapter
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
        ordersAdapter = OrdersAdapter()
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
                    }

                    else -> {
                        Toast.makeText(requireContext(), "No Connection", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun stateObserve() {
        lifecycleScope.launch {
            viewModel.ordersState.collectLatest {

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

    private fun setOrdersRecycler() {
        val ordersLayoutManager = LinearLayoutManager(requireContext())
        ordersLayoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.ordersRv.apply {
            adapter = ordersAdapter
            layoutManager = ordersLayoutManager
        }
    }
}