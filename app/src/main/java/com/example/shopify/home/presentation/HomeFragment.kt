package com.example.shopify.home.presentation

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopify.R
import com.example.shopify.databinding.BottomSheetLayoutBinding
import com.example.shopify.databinding.FragmentHomeBinding
import com.example.shopify.home.domain.model.BrandModel
import com.example.shopify.home.domain.model.ProductModel
import com.example.shopify.utils.connectivity.ConnectivityObserver
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment(private val connectivityObserver: ConnectivityObserver) : Fragment() {


    private lateinit var binding: FragmentHomeBinding
    private lateinit var navController: NavController
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var brandsAdapter: BrandsAdapter
    private lateinit var productsAdapter: ProductsAdapter
    private var vendor: String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        binding.filterImageButton.setOnClickListener {
            showBottomDialog()
        }
        binding.cartImageButton.setOnClickListener {
            navController.setGraph(R.navigation.settings_graph)
            navController.navigate(Uri.parse(getString(R.string.cartFragmentDeepLink)))
        }
        brandsAdapter = BrandsAdapter(requireContext()) {
            getProductsByBrand(it)
        }
        productsAdapter = ProductsAdapter(requireContext()) {
            goToProductsInfo(it)
        }
        setProductsRecycler()
        setBrandsRecycler()
        checkConnection()
        stateObserve()
    }

    private fun showBottomDialog() {
        val bottomSheet = BottomSheetLayoutBinding.inflate(layoutInflater)
        val dialog = Dialog(requireContext())
        dialog.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(bottomSheet.root)

            bottomSheet.applyBtn.setOnClickListener {
                dismiss()
            }

            window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window?.attributes?.windowAnimations = R.style.DialogAnimation
            window?.setGravity(Gravity.BOTTOM)

        }.show()
    }

    private fun checkConnection() {
        lifecycleScope.launch {
            connectivityObserver.observe().collectLatest {
                when (it) {
                    ConnectivityObserver.Status.Available -> {
                        viewModel.getAllProducts()
                        viewModel.getAllBrands()
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
            viewModel.homeState.collectLatest {

                if (it.brands.isNotEmpty()) {
                    brandsAdapter.submitList(it.brands)
                }
                if (it.products.isNotEmpty()) {
                    productsAdapter.submitList(it.products)
                }else{
                    productsAdapter.submitList(listOf())
                }
            }
        }

    }

    private fun getProductsByBrand(brandModel: BrandModel) {
        Toast.makeText(requireContext(), brandModel.title, Toast.LENGTH_SHORT).show()
        vendor = brandModel.title
        viewModel.getAllProducts(vendor)
        vendor = ""
    }

    private fun goToProductsInfo(product: ProductModel) {
        Toast.makeText(requireContext(), product.title, Toast.LENGTH_SHORT).show()
    }

    private fun setBrandsRecycler() {
        val brandsLayoutManager = LinearLayoutManager(requireContext())
        brandsLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        binding.brandRv.apply {
            adapter = brandsAdapter
            layoutManager = brandsLayoutManager
        }
    }

    private fun setProductsRecycler() {
        val productsLayoutManager = GridLayoutManager(requireContext(),2)
        productsLayoutManager.orientation = GridLayoutManager.VERTICAL
        binding.productsRv.apply {
            adapter = productsAdapter
            layoutManager = productsLayoutManager
        }
    }

}