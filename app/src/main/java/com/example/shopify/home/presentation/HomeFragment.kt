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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
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
import com.example.shopify.utils.ui.visibleIf
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.slider.RangeSlider
import com.google.android.material.slider.RangeSlider.OnSliderTouchListener
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Currency

@AndroidEntryPoint
class HomeFragment(private val connectivityObserver: ConnectivityObserver) : Fragment() {


    private lateinit var binding: FragmentHomeBinding
    private lateinit var navController: NavController
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var brandsAdapter: BrandsAdapter
    private lateinit var productsAdapter: ProductsAdapter
    private var vendor: String = ""
    private var currency: String = "EGP"
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
        binding.ordersImageButton.setOnClickListener {
            navController.navigate(HomeFragmentDirections.actionHomeFragmentToOrdersFragment())
        }
        brandsAdapter = BrandsAdapter(requireContext()) {
            getProductsByBrand(it)
        }
        productsAdapter = ProductsAdapter {
            goToProductsInfo(it)
        }


        binding.addsCardView.setOnClickListener {
            viewModel.homeState.value.discountCode?.let {
                showDiscountCodeDialog(it.code)
            }
        }
        setProductsRecycler()
        setBrandsRecycler()
        checkConnection()
        stateObserve()
    }

    private fun showBottomDialog() {
        val bottomSheet = BottomSheetLayoutBinding.inflate(layoutInflater)
        val dialog = Dialog(requireContext())
        var selectedCategory: Long? = null
        var selectedType = ""
        var max = Double.MAX_VALUE
        var min = Double.MIN_VALUE
        dialog.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(bottomSheet.root)

            bottomSheet.categoryRg.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.menRb -> {
                        selectedCategory = bottomSheet.menRb.tag.toString().toLong()
                    }

                    R.id.womenRb -> {
                        selectedCategory = bottomSheet.womenRb.tag.toString().toLong()
                    }

                    R.id.kidsRb -> {
                        selectedCategory = bottomSheet.kidsRb.tag.toString().toLong()
                    }

                    R.id.saleRb -> {
                        selectedCategory = bottomSheet.saleRb.tag.toString().toLong()
                    }
                }
            }

            bottomSheet.typesRg.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.tShirtsRb -> {
                        selectedType = bottomSheet.tShirtsRb.tag.toString()
                    }

                    R.id.accessoriesRb -> {
                        selectedType = bottomSheet.accessoriesRb.tag.toString()
                    }

                    R.id.shoesRb -> {
                        selectedType = bottomSheet.shoesRb.tag.toString()
                    }
                }
            }

            bottomSheet.priceRangeSlider.setLabelFormatter {
                val format = NumberFormat.getCurrencyInstance()
                format.maximumFractionDigits = 0
                format.currency = Currency.getInstance(currency)
                format.format(it.toDouble())
            }
            bottomSheet.priceRangeSlider.addOnSliderTouchListener(object : OnSliderTouchListener {
                override fun onStartTrackingTouch(slider: RangeSlider) {
                }

                override fun onStopTrackingTouch(slider: RangeSlider) {
                    max = slider.values.max().toDouble()
                    min = slider.values.min().toDouble()
                }

            })


            bottomSheet.applyBtn.setOnClickListener {
                if (selectedCategory != null || selectedType.isNotEmpty() || max != Double.MAX_VALUE || min != Double.MIN_VALUE) {
                    viewModel.filterProducts(selectedCategory, selectedType, max, min)
                    brandsAdapter.clearSelection()
                }
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
        val connectivitySnackBar = Snackbar.make(
            binding.root, getString(com.firebase.ui.auth.R.string.fui_no_internet),
            Snackbar.LENGTH_INDEFINITE
        )
        lifecycleScope.launch {
            connectivityObserver.observe().collectLatest {
                delay(200)
                when (it) {
                    ConnectivityObserver.Status.Available -> {
                        connectivitySnackBar.dismiss()
                        viewModel.readCurrencyFactorFromDataStore()
                        viewModel.getAllProducts()
                        viewModel.getAllBrands()
                    }

                    else -> {
                        connectivitySnackBar.show()
                    }
                }
            }
        }
    }

    private fun stateObserve() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {

                viewModel.homeState.collectLatest {

                    if (it.brands.isNotEmpty()) {
                        brandsAdapter.submitList(it.brands)
                    }
                    if (it.products.isNotEmpty()) {
                        productsAdapter.submitList(it.products)
                    } else {
                        productsAdapter.submitList(listOf())
                    }
                    binding.progressBar visibleIf it.loading
                    if (it.currency != "EGP") {
                        currency = it.currency
                        productsAdapter.exchangeRate = it.exchangeRate
                        productsAdapter.currency = it.currency
                    }

                    binding.addsCardView visibleIf (it.discountCode != null)

                }
            }
        }

    }

    private fun showDiscountCodeDialog(code: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(getString(R.string.free_discount_code) + " " + code)
            .setNegativeButton(getString(R.string.cancel)) { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .setNeutralButton(getString(R.string.save)) { _, _ ->
                viewModel.insertDiscountCode()
            }
            .show()
    }

    private fun getProductsByBrand(brandModel: BrandModel) {
        Toast.makeText(requireContext(), brandModel.title, Toast.LENGTH_SHORT).show()
        vendor = brandModel.title
        viewModel.getProductsByBrand(vendor)
        vendor = ""
    }

    private fun goToProductsInfo(product: ProductModel) {
        val uri = Uri.parse("shopify://productDetailsFragment/${product.id}")
        navController.navigate(uri)
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
        val productsLayoutManager = GridLayoutManager(requireContext(), 2)
        productsLayoutManager.orientation = GridLayoutManager.VERTICAL
        binding.productsRv.apply {
            adapter = productsAdapter
            layoutManager = productsLayoutManager
        }
    }

}