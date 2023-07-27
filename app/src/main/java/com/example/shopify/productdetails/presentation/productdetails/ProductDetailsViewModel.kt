package com.example.shopify.productdetails.presentation.productdetails

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopify.R
import com.example.shopify.domain.usecase.dataStore.ReadStringFromDataStoreUseCase
import com.example.shopify.productdetails.data.dto.draftorder.DraftOrderRequest
import com.example.shopify.productdetails.domain.model.details.ProductsDetailsModel
import com.example.shopify.productdetails.domain.usecase.AddToCartUseCase
import com.example.shopify.productdetails.domain.usecase.AddToFavoritesUseCase
import com.example.shopify.productdetails.domain.usecase.GetProductDetailsUseCase
import com.example.shopify.utils.hiltanotations.Dispatcher
import com.example.shopify.utils.hiltanotations.Dispatchers
import com.example.shopify.utils.response.Response
import com.example.shopify.utils.rounder.roundTo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    @Dispatcher(Dispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    private val getProductDetailsUseCase: GetProductDetailsUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val addToFavoritesUseCase: AddToFavoritesUseCase,
    private val readStringFromDataStoreUseCase: ReadStringFromDataStoreUseCase

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
            is ProductDetailsIntent.AddToCart -> addToCart(intent.variantId, intent.product)

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
                        Timber.i("DATA (Options): " + response.data?.options)

                        _state.update { it.copy(product = response.data, loading = false) }

                        readCurrencyFactorFromDataStore()

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
                        _state.update { it.copy(loading = false) }

                        when (response.error) {
                            "itemAlreadyExistException" -> {
                                _snackBarFlow.emit(
                                    R.string.already_in_fav
                                )
                                _state.update { it.copy(isFavorite = true) }
                            }

                            else -> _snackBarFlow.emit(R.string.failed_message)
                        }
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

    private fun addToCart(variantId: Long?, product: ProductsDetailsModel) {

        viewModelScope.launch(ioDispatcher) {
            addToCartUseCase<ProductsDetailsModel>(variantId, product).collectLatest { response ->
                _state.update { it.copy(loading = true) }

                when (response) {
                    is Response.Failure -> {
                        _state.update { it.copy(loading = false) }
                        Timber.i("ERROR: ${response.error}")
                        when (response.error) {
                            "itemAlreadyExistException" -> {
                                _snackBarFlow.emit(R.string.already_in_cart)
                                _state.update { it.copy(isCartItem = true) }
                            }

                            else -> _snackBarFlow.emit(R.string.failed_message)
                        }
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

    private fun readCurrencyFactorFromDataStore() {
        viewModelScope.launch(ioDispatcher) {
            readStringFromDataStoreUseCase.execute<String>("currencyFactor")
                .combine(readStringFromDataStoreUseCase.execute<String>("currency")) { currencyFactor, currency ->
                    Log.d("currencyFactor", currencyFactor.data.toString())
                    when (currencyFactor) {
                        is Response.Failure -> {}
                        is Response.Loading -> {}
                        is Response.Success -> {
                            _state.update {
                                it.copy(
                                    currencyFactor = currencyFactor.data?.toDouble() ?: 1.0,
                                    currency = currency.data ?: "EGP"
                                )
                            }
                            _state.value.product?.variants?.first()?.price =
                                (_state.value.product?.variants?.first()?.price?.toDouble()
                                    ?.times(_state.value.currencyFactor))?.roundTo(2).toString()
                            _state.value.product?.currency = currency.data ?: "EGP"

                        }
                    }
                }.collect()
        }
    }

}
