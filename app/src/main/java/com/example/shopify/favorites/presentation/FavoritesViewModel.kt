package com.example.shopify.favorites.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopify.R
import com.example.shopify.domain.usecase.GetDraftOrdersUseCase
import com.example.shopify.domain.usecase.RemoveDraftOrderUseCase
import com.example.shopify.favorites.domain.model.FavoritesModel
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
class FavoritesViewModel @Inject constructor(
    @Dispatcher(Dispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    private val getFavoritesUseCase: GetDraftOrdersUseCase,
    private val removeDraftOrderUseCase: RemoveDraftOrderUseCase
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
            is FavoritesIntent.RemoveFromFavorites -> removeItem(intent.id)
        }
    }


    fun getFavorites() {
        Timber.i("GET Favorites ")
        viewModelScope.launch(ioDispatcher) {
            getFavoritesUseCase<FavoritesModel>().collectLatest { response ->
                when (response) {

                    is Response.Failure -> {
                        _snackBarFlow.emit(R.string.failed_message)
                    }


                    is Response.Loading -> {
                        _state.update { it.copy(loading = true) }
                    }

                    is Response.Success -> {
                        val productList = response.data?.products

                        if (productList.isNullOrEmpty()) {

                            _state.update { it.copy(empty = true) }

                        } else {

                            _state.update { it.copy(empty = true, loading = false) }
                            Timber.i(productList.toString())
                        }
                    }

                }
            }
        }

    }

    fun removeItem(id: String) {
        viewModelScope.launch(ioDispatcher) {

            removeDraftOrderUseCase(id).collectLatest { response ->
                when (response) {
                    is Response.Success -> _state.update { it.copy(deleted = true) }
                    is Response.Loading -> _state.update { it.copy(loading = true) }
                    is Response.Failure -> _snackBarFlow.emit(R.string.failed_message)
                }
            }
        }
    }
}