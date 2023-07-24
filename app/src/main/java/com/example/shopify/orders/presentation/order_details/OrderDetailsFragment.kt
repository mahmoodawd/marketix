package com.example.shopify.orders.presentation.order_details

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopify.R
import com.example.shopify.databinding.FragmentOrderDetailsBinding

class OrderDetailsFragment : Fragment() {
    private lateinit var binding: FragmentOrderDetailsBinding
    private lateinit var navController: NavController
    private lateinit var lineItemsAdapter: LineItemsAdapter
    private val args: OrderDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentOrderDetailsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        lineItemsAdapter = LineItemsAdapter {
            goToProductDetails(it)
        }
        setProductsRecycler()

        binding.order = args.order

        lineItemsAdapter.submitList(args.order.lineItems)

        binding.backImageView.setOnClickListener {
            navController.navigateUp()
        }
    }

    private fun goToProductDetails(productId: Long) {
        val uri = Uri.parse("shopify://productDetailsFragment/${productId}")
        navController.navigate(uri)
    }

    private fun setProductsRecycler() {
        val productsLayoutManager = LinearLayoutManager(requireContext())
        productsLayoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.productsRv.apply {
            adapter = lineItemsAdapter
            layoutManager = productsLayoutManager
        }
    }
}