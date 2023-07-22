package com.example.shopify.productdetails.presentation.productdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.example.shopify.databinding.FragmentProductDetailsBinding
import com.example.shopify.utils.snackBarObserver
import com.example.shopify.utils.ui.visibleIf
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class ProductDetailsFragment : Fragment() {

    lateinit var binding: FragmentProductDetailsBinding

    val viewModel: ProductDetailsViewModel by viewModels()

    private val args: ProductDetailsFragmentArgs by navArgs()

    val imagesAdapter: ProductImagesAdapter by lazy {
        ProductImagesAdapter(requireContext())
    }
    val sizesAdapter: ProductSizesAdapter by lazy {
        ProductSizesAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        binding = FragmentProductDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.parentFab.setOnClickListener {

            binding.isFabVisible = false

        }

        viewModel.onEvent(ProductDetailsIntent.GetDetails(args.productId))

        observeState()

        snackBarObserver(viewModel.snackBarFlow)
    }

    private fun observeState() {
        lifecycleScope.launch(Dispatchers.IO) {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.state.collectLatest { state ->

                    withContext(Dispatchers.Main) {

                        binding.progressBar visibleIf state.loading

                        binding.product = state.product

                        imagesAdapter.submitList(state.product?.images)

                        sizesAdapter.submitList(listOf("45", "25", "14"))
                    }
                }
            }
        }
    }

}