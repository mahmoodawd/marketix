package com.example.shopify.favorites.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopify.R
import com.example.shopify.auth.domain.usecases.CheckGuestStatusUseCase
import com.example.shopify.domain.usecase.dataStore.ReadStringFromDataStoreUseCase
import com.example.shopify.favorites.domain.model.FavoritesModel
import com.example.shopify.favorites.domain.usecase.GetFavoriteProductsUseCase
import com.example.shopify.favorites.domain.usecase.RemoveDraftOrderUseCase
import com.example.shopify.search.domain.model.SearchProductsModel
import com.example.shopify.search.domain.usecase.GetSearchResultUseCase
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
class FavoritesViewModel @Inject constructor(
    @Dispatcher(Dispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    private val getFavoritesUseCase: GetFavoriteProductsUseCase,
    private val removeDraftOrderUseCase: RemoveDraftOrderUseCase,
    private val getSearchResultUseCase: GetSearchResultUseCase,
    private val readStringFromDataStoreUseCase: ReadStringFromDataStoreUseCase,
    private val checkGuestStatusUseCase: CheckGuestStatusUseCase


) : ViewModel() {

    private val _state: MutableStateFlow<FavoritesState> =
        MutableStateFlow(FavoritesState())
    val state = _state.asStateFlow()

    private val _snackBarFlow: MutableSharedFlow<Int> =
        MutableSharedFlow()
    val snackBarFlow = _snackBarFlow.asSharedFlow()


    fun onEvent(intent: FavoritesIntent) {
        when (intent) {
            is FavoritesIntent.GetFavorites -> getFavorites()
            is FavoritesIntent.RemoveFromFavorites -> removeItem(intent.id, intent.itemPosition)
            is FavoritesIntent.Search -> search(intent.keyword)
        }
    }


    private fun getFavorites() {
        viewModelScope.launch(ioDispatcher) {
            if (checkGuestStatusUseCase()) {
                _state.update { it.copy(loading = false, guest = true) }
            } else {
                _state.update { it.copy(guest = false) }
                getFavoritesUseCase<FavoritesModel>().collectLatest { response ->
                    Timber.i(" RESPONSE: ${response.data?.products?.size}")
                    when (response) {

                        is Response.Failure -> {
                            _snackBarFlow.emit(R.string.failed_message)

                            Timber.i(" ERROR: ${response.error}")

                            _state.update { it.copy(loading = false) }
                        }


                        is Response.Loading -> {
                            _state.update { it.copy(loading = true) }
                        }

                        is Response.Success -> {

                            _state.update {
                                it.copy(
                                    loading = false,
                                    products = response.data?.products,
                                )


                            }

                            readCurrencyFactorFromDataStore()
                        }

                    }
                }
            }
        }

    }

    private fun removeItem(id: String, itemPosition: Int) {
        viewModelScope.launch(ioDispatcher) {

            removeDraftOrderUseCase(id).collectLatest { response ->
                Timber.i("response: ${response.data}")
                when (response) {
                    is Response.Success -> {
                        _state.update {
                            it.copy(
                                loading = false,
                                products = _state.value.products?.toMutableList()
                                    ?.filter { item -> item.id.toString() != id }
                            )
                        }
                        _snackBarFlow.emit(R.string.item_deleted_successfully)
                        getFavorites()

                    }

                    is Response.Loading -> _state.update { it.copy(loading = true) }
                    is Response.Failure -> _snackBarFlow.emit(R.string.failed_message)
                }
            }
        }
    }

    private fun readCurrencyFactorFromDataStore() {
        viewModelScope.launch(ioDispatcher) {
            readStringFromDataStoreUseCase.execute<String>("currencyFactor")
                .combine(readStringFromDataStoreUseCase.execute<String>("currency")) { currencyFactor, currency ->
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
                            _state.value.products?.forEach { product ->
                                product.price = ((product.price.toDouble())
                                        * _state.value.currencyFactor).roundTo(2).toString()

                                product.currency = currency.data ?: "EGP"
                            }
                        }
                    }
                }.collect()
        }
    }

    private fun search(keyword: String) {
        viewModelScope.launch {

            getSearchResultUseCase<SearchProductsModel>(keyword).collectLatest { response ->
                when (response) {
                    is Response.Success -> {
                        response.data!!.products.filter { product ->
                            product.title.contains(keyword, true)
                        }.let {
                            _state.update { uiState ->
                                uiState.copy(searchResult = it, loading = false)
                            }

                            Timber.i("Search Results: ${it}\n_________")
                        }

                    }

                    is Response.Failure -> {
                        Timber.i("ERROR: ${response.error}")
                    }

                    is Response.Loading -> {}
                }
            }
        }
    }
}
