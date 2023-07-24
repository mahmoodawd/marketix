package com.example.shopify.productdetails.presentation.productdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopify.R
import com.example.shopify.productdetails.domain.model.ProductsDetailsModel
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
    private val getProductDetailsUseCase: GetProductDetailsUseCase
) : ViewModel() {

    private val _state: MutableStateFlow<ProductDetailsState> =
        MutableStateFlow(ProductDetailsState())
    val state = _state.asStateFlow()

    private val _snackBarFlow: MutableSharedFlow<Int> =
        MutableSharedFlow()
    val snackBarFlow = _snackBarFlow.asSharedFlow()


    fun onEvent(intent: ProductDetailsIntent) {
        when (intent) {
            is ProductDetailsIntent.GetDetails -> getProductDetails(intent.productId)

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
}