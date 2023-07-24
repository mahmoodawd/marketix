package com.example.shopify.productdetails.presentation.productdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopify.R
import com.example.shopify.productdetails.data.dto.draftorder.DraftOrderRequest
import com.example.shopify.productdetails.domain.model.details.ProductsDetailsModel
import com.example.shopify.productdetails.domain.usecase.AddToCartUseCase
import com.example.shopify.productdetails.domain.usecase.AddToFavoritesUseCase
import com.example.shopify.productdetails.domain.usecase.GetProductDetailsUseCase
import com.example.shopify.utils.hiltanotations.Dispatcher
import com.example.shopify.utils.hiltanotations.Dispatchers
import com.example.shopify.utils.response.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    @Dispatcher(Dispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    private val getProductDetailsUseCase: GetProductDetailsUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val addToFavoritesUseCase: AddToFavoritesUseCase
) : ViewModel() {

    private val _state: MutableStateFlow<ProductDetailsState> =
        MutableStateFlow(ProductDetailsState())
    val state = _state.asStateFlow()

    private val _snackBarFlow: MutableSharedFlow<Int> = MutableSharedFlow()
    val snackBarFlow = _snackBarFlow.asSharedFlow()


    fun onEvent(intent: ProductDetailsIntent) {
        when (intent) {
            is ProductDetailsIntent.GetDetails -> getProductDetails(intent.productId)
            is ProductDetailsIntent.AddToFavorite -> addToFavorites(intent.product)
            is ProductDetailsIntent.AddToCart -> addToCart(intent.product)

        }
    }


    private fun getProductDetails(id: String) {

        viewModelScope.launch(ioDispatcher) {

            getProductDetailsUseCase<ProductsDetailsModel>(id).collectLatest { response ->
                _state.update { it.copy(loading = true) }

                when (response) {
                    is Response.Failure -> {

                        _snackBarFlow.emit(R.string.failed_message)

                        Timber.i("ERROR: " + response.error)

                        _state.update { it.copy(loading = false) }
                    }

                    is Response.Loading -> {
                        _state.update { it.copy(loading = true) }
                    }

                    is Response.Success -> {
                        Timber.i("DATA: " + response.data)

                        _state.update { it.copy(product = response.data, loading = false) }

                    }
                }

            }
        }
    }

    private fun addToFavorites(productsDetailsModel: ProductsDetailsModel) {

        viewModelScope.launch(ioDispatcher) {
            addToFavoritesUseCase<DraftOrderRequest>(productsDetailsModel).collectLatest { response ->
                _state.update { it.copy(loading = true) }

                when (response) {
                    is Response.Failure -> {
                        Timber.i("ERROR: ${response.error}")
                        _snackBarFlow.emit(R.string.failed_message)
                        _state.update { it.copy(loading = false) }
                    }

                    is Response.Loading -> _state.update { it.copy(loading = true) }

                    is Response.Success -> {

                        _state.update { it.copy(loading = false, isFavorite = true) }
                        _snackBarFlow.emit(R.string.added_to_favorites)
                    }
                }
            }
        }
    }

    private fun addToCart(productsDetailsModel: ProductsDetailsModel) {

        viewModelScope.launch(ioDispatcher) {
            addToCartUseCase<ProductsDetailsModel>(productsDetailsModel).collectLatest { response ->
                _state.update { it.copy(loading = true) }
                when (response) {
                    is Response.Failure -> {
                        _snackBarFlow.emit(R.string.failed_message)
                        _state.update { it.copy(loading = false) }
                        Timber.i("ERROR: ${response.error}")
                    }

                    is Response.Loading -> _state.update { it.copy(loading = true) }

                    is Response.Success -> {

                        _state.update { it.copy(loading = false, isCartItem = true) }
                        _snackBarFlow.emit(R.string.added_to_cart)
                    }
                }
            }
        }
    }
}
