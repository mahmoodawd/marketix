package com.example.shopify.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopify.home.domain.model.BrandsModel
import com.example.shopify.home.domain.model.ProductsModel
import com.example.shopify.home.domain.usecase.FilterByPriceUseCase
import com.example.shopify.home.domain.usecase.FilterProductsUseCase
import com.example.shopify.home.domain.usecase.GetAllBrandsUseCase
import com.example.shopify.home.domain.usecase.GetAllProductsUseCase
import com.example.shopify.home.domain.usecase.GetProductsByBrandUseCase
import com.example.shopify.utils.hiltanotations.Dispatcher
import com.example.shopify.utils.hiltanotations.Dispatchers
import com.example.shopify.utils.response.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
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
    private val filerByPriceUseCase: FilterByPriceUseCase
) : ViewModel() {

    private val _homeState: MutableStateFlow<HomeState.Display> =
        MutableStateFlow(HomeState.Display())
    val homeState = _homeState.asStateFlow()

    fun getAllBrands() {
        viewModelScope.launch(ioDispatcher) {
            _homeState.update { it.copy(loading = true) }
            getAllBrandsUseCase.execute<BrandsModel>().collectLatest { response ->
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
            getAllProductsUseCase.execute<ProductsModel>().collectLatest { response ->
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
            getProductsByBrandUseCase.execute<ProductsModel>(brand).collectLatest { response ->
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
            filterProductsUseCase.execute<ProductsModel>(category, productType)
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
}