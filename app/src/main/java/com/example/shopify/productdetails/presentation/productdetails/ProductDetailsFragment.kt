package com.example.shopify.productdetails.presentation.productdetails

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
import androidx.navigation.fragment.navArgs
import com.example.shopify.R
import com.example.shopify.databinding.FragmentProductDetailsBinding
import com.example.shopify.productdetails.domain.model.details.ProductsDetailsModel
import com.example.shopify.productdetails.presentation.productdetails.options.OptionAdapter
import com.example.shopify.utils.snackBarObserver
import com.example.shopify.utils.ui.visibleIf
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

@AndroidEntryPoint
class ProductDetailsFragment : Fragment() {

    lateinit var binding: FragmentProductDetailsBinding

    val viewModel: ProductDetailsViewModel by viewModels()

    val navController: NavController by lazy {
        this@ProductDetailsFragment.findNavController()
    }

    private val args: ProductDetailsFragmentArgs by navArgs()

    private var isFav = false

    private var isCartItem = false

    private var currentProduct: ProductsDetailsModel? = null

    private val imagesAdapter: ProductImagesAdapter by lazy {
        ProductImagesAdapter()
    }

    private val optionAdapter: OptionAdapter by lazy {
        OptionAdapter()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        binding = FragmentProductDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageAdapter = imagesAdapter
        binding.optionAdapter = optionAdapter

        binding.indicator.attachToRecyclerView(binding.productImagesViewPager)

        binding.backImageView.setOnClickListener { navController.popBackStack() }

        binding.addToFavFab.setOnClickListener {

            viewModel.onEvent(ProductDetailsIntent.AddToFavorite(binding.product!!))
        }


        binding.addToCartFab.setOnClickListener {
            var title = ""
            optionAdapter.selectedOptions.forEachIndexed { index, option ->
                title = title.plus(option)
                if (index < optionAdapter.selectedOptions.size - 1) {
                    title = title.plus(" / ")
                }

            }
            //Remove the trailing "/" of the last options to match variant title format(option1 / option2 / option3)
            title.dropLast(3)
            Timber.i("Selected Option after appending = $title")

            if (optionAdapter.selectedOptions.size == optionAdapter.currentList.size) {

                val selectedVariantId: Long? = try {

                    currentProduct?.variants?.first {
                        it.title == title
                    }?.id
                } catch (e: NoSuchElementException) {
                    Toast.makeText(
                        requireContext(),
                        "Sorry!, No variants match $title",
                        Toast.LENGTH_SHORT
                    ).show()
                    0L
                }

                Timber.i("Selected Variant ID: $selectedVariantId")
                when (selectedVariantId) {
                    null, 0L -> {}
                    else -> {
                        viewModel.onEvent(
                            ProductDetailsIntent.AddToCart(
                                selectedVariantId, binding.product!!
                            )
                        )
                    }
                }
                optionAdapter.resetSelections()
            } else {
                Toast.makeText(requireContext(), "Select Options First", Toast.LENGTH_SHORT).show()
            }
        }


        binding.ratingView.apply {
            ratingBar.rating = 3.5F
            toReviewsIv.setOnClickListener {
                navController.navigate(
                    R.id.action_productDetailsFragment_to_reviewsFragment
                )
            }
        }

        viewModel.onEvent(ProductDetailsIntent.GetDetails(args.productId))

        requireActivity().snackBarObserver(viewModel.snackBarFlow)

        observeState()
    }


    private fun setFabsColors(cart: Boolean = false, favorite: Boolean = false) {
        val existIconColor = resources.getColorStateList(
            R.color.md_theme_dark_onPrimaryContainer, requireContext().theme
        )
        val existBackColor = resources.getColorStateList(
            R.color.md_theme_dark_inversePrimary, requireContext().theme
        )

        binding.addToCartFab.apply {

            backgroundTintList = if (cart) existBackColor else existIconColor
            iconTint = if (cart) existIconColor else existBackColor
            if (cart) setTextColor(existIconColor) else setTextColor(existBackColor)


        }
        binding.addToFavFab.apply {

            backgroundTintList = if (favorite) existBackColor else existIconColor
            iconTint = if (favorite) existIconColor else existBackColor
            if (favorite) setTextColor(existIconColor) else setTextColor(existBackColor)


        }

    }

    private fun observeState() {
        lifecycleScope.launch(Dispatchers.IO) {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.state.collectLatest { state ->

                    withContext(Dispatchers.Main) {

                        binding.progressBar visibleIf state.loading

                        isFav = state.isFavorite
                        isCartItem = state.isCartItem
                        setFabsColors(favorite = state.isFavorite, cart = state.isCartItem)
                        state.product?.run {

                            binding.product = this
                            currentProduct = this

                            imagesAdapter.submitList(images)
                            Timber.i("Image List: $images")
                            optionAdapter.submitList(options)
                        }
                    }
                }
            }
        }
    }
}
