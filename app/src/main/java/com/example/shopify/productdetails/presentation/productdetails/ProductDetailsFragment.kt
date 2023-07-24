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
import androidx.viewpager2.widget.ViewPager2
import com.example.shopify.R
import com.example.shopify.databinding.FragmentProductDetailsBinding
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

    private val args: ProductDetailsFragmentArgs by navArgs()

    private var isFav = false
    private var isCartItem = false

    lateinit var imagesAdapter: ProductImagesAdapter

    private val sizesAdapter: ProductSizesAdapter by lazy {
        ProductSizesAdapter().apply {
            binding.sizesAdapter = this

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        binding = FragmentProductDetailsBinding.inflate(inflater, container, false)

        imagesAdapter = ProductImagesAdapter()
        binding.imageAdapter = imagesAdapter

        binding.productImagesViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
            }
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addToFavFab.setOnClickListener {

            viewModel.onEvent(ProductDetailsIntent.AddToFavorite(binding.product!!))
        }

        binding.addToCartFab.setOnClickListener {
            viewModel.onEvent(ProductDetailsIntent.AddToCart(binding.product!!))
        }


        viewModel.onEvent(ProductDetailsIntent.GetDetails(args.productId))

        observeState()

        snackBarObserver(viewModel.snackBarFlow)
    }

    private fun setFabsColors(cart: Boolean = false, favorite: Boolean = false) {
        val existIconColor = resources.getColorStateList(
            R.color.md_theme_dark_onPrimaryContainer,
            requireContext().theme
        )
        val existBackColor = resources.getColorStateList(
            R.color.md_theme_dark_inversePrimary,
            requireContext().theme
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

                            imagesAdapter.submitList(images)
                            Timber.i("Image List: $images")

                            sizesAdapter.submitList(options?.first()?.values)
                        }
                    }
                }
            }
        }
    }

}