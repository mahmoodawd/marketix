package com.example.shopify.home.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopify.R
import com.example.shopify.home.domain.model.discountcode.DiscountCodesModel
import com.example.shopify.home.domain.usecase.FilterByPriceUseCase
import com.example.shopify.home.domain.usecase.FilterProductsUseCase
import com.example.shopify.home.domain.usecase.GetAllBrandsUseCase
import com.example.shopify.home.domain.usecase.GetAllProductsUseCase
import com.example.shopify.home.domain.usecase.GetProductsByBrandUseCase
import com.example.shopify.home.domain.usecase.discount.GetDiscountCodesUseCase
import com.example.shopify.home.domain.usecase.discount.InsertDiscountCodesUseCase
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
class HomeViewModel @Inject constructor(
    @Dispatcher(Dispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    private val getAllBrandsUseCase: GetAllBrandsUseCase,
    private val getAllProductsUseCase: GetAllProductsUseCase,
    private val getProductsByBrandUseCase: GetProductsByBrandUseCase,
    private val filterProductsUseCase: FilterProductsUseCase,
    private val filerByPriceUseCase: FilterByPriceUseCase,
    private val getDiscountCodesUseCase: GetDiscountCodesUseCase,
    private val insertDiscountCodesUseCase: InsertDiscountCodesUseCase
) : ViewModel() {

    private val _homeState: MutableStateFlow<HomeState.Display> =
        MutableStateFlow(HomeState.Display())
    val homeState = _homeState.asStateFlow()


    private val _snackBarFlow: MutableSharedFlow<Int> =
        MutableSharedFlow()
    val snackBarFlow = _snackBarFlow.asSharedFlow()


    fun getAllBrands() {
        viewModelScope.launch(ioDispatcher) {
            _homeState.update { it.copy(loading = true) }
            getAllBrandsUseCase.execute().collectLatest { response ->
                when (response) {
                    is Response.Success -> {
                        response.data?.let {
                            _homeState.update {
                                it.copy(brands = response.data.brands, loading = false)
                            }
                        }
                    }

                    is Response.Failure -> {
                        Timber.e(response.error)
                        _homeState.update {
                            it.copy(error = response.error, loading = false)
                        }
                    }

                    is Response.Loading -> {
                        _homeState.update {
                            it.copy(loading = true)
                        }
                    }
                }
            }
        }
    }

    fun getAllProducts() {
        Timber.e("getAllProducts")
        viewModelScope.launch(ioDispatcher) {
            _homeState.update { it.copy(loading = true) }
            getAllProductsUseCase.execute().collectLatest { response ->
                when (response) {
                    is Response.Success -> {
                        Timber.e("success")
                        response.data?.let {
                            _homeState.update {
                                it.copy(products = response.data.products, loading = false)
                            }
                        }
                    }

                    is Response.Failure -> {
                        Timber.e("fail")
                        _homeState.update {
                            it.copy(error = response.error, loading = false)
                        }
                    }

                    is Response.Loading -> {
                        _homeState.update {
                            it.copy(loading = true)
                        }
                    }
                }
            }
        }
    }

    fun getProductsByBrand(brand: String) {
        Timber.e("getAllProducts")
        viewModelScope.launch(ioDispatcher) {
            _homeState.update { it.copy(loading = true) }
            getProductsByBrandUseCase.execute(brand).collectLatest { response ->
                when (response) {
                    is Response.Success -> {
                        Timber.e("success")
                        response.data?.let {
                            _homeState.update {
                                it.copy(products = response.data.products, loading = false)
                            }
                        }
                    }

                    is Response.Failure -> {
                        Timber.e("fail")
                        _homeState.update {
                            it.copy(error = response.error, loading = false)
                        }
                    }

                    is Response.Loading -> {
                        _homeState.update {
                            it.copy(loading = true)
                        }
                    }
                }
            }
        }
    }

    fun filterProducts(
        category: Long?,
        productType: String,
        max: Double,
        min: Double
    ) {
        Timber.e("getProductsByType")
        viewModelScope.launch(ioDispatcher) {
            _homeState.update { it.copy(loading = true) }
            filterProductsUseCase.execute(category, productType)
                .collectLatest { response ->
                    when (response) {
                        is Response.Success -> {
                            Timber.e("success")
                            response.data?.let {
                                _homeState.update {
                                    it.copy(
                                        products = filerByPriceUseCase(
                                            max,
                                            min,
                                            response.data.products
                                        ), loading = false
                                    )
                                }
                            }
                        }

                        is Response.Failure -> {
                            Timber.e("fail")
                            Timber.e(response.error)
                            _homeState.update {
                                it.copy(error = response.error, loading = false)
                            }
                        }

                        is Response.Loading -> {
                            _homeState.update {
                                it.copy(loading = true)
                            }
                        }
                    }
                }
        }
    }

   private fun getDiscountCodes() {
        viewModelScope.launch(ioDispatcher) {
            getDiscountCodesUseCase.execute<DiscountCodesModel>().collectLatest { response ->
                when (response) {
                    is Response.Failure -> {}
                    is Response.Loading -> _homeState.update { it.copy(loading = true) }
                    is Response.Success -> {
                        _homeState.update {
                            it.copy(
                                discountCode = response.data?.discountCodes?.shuffled()?.first()
                            )
                        }
                    }
                }
            }
        }
    }


    fun insertDiscountCode() {
        viewModelScope.launch(ioDispatcher) {
            Log.d("codeBefore",_homeState.value.discountCode.toString())
            insertDiscountCodesUseCase.execute<String>(_homeState.value.discountCode!!)
                .collectLatest { response ->
                    initDiscountCodeHomeState()
                    when(response){
                        is Response.Failure -> {
                            _snackBarFlow.emit(R.string.failed_message)
                        }
                        is Response.Loading -> {}
                        is Response.Success -> {

                            _snackBarFlow.emit(R.string.inserted_successfully)
                        }
                    }
                }
        }

    }

    fun initDiscountCodeHomeState()
    {
        _homeState.update { it.copy(discountCode = null) }
    }

    init {
        getDiscountCodes()
    }
}