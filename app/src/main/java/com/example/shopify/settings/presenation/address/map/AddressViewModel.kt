package com.example.shopify.settings.presenation.address.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopify.R
import com.example.shopify.settings.domain.usecase.dataStore.ReadStringFromDataStoreUseCase
import com.example.shopify.settings.domain.usecase.dataStore.SaveStringToDataStoreUseCase
import com.example.shopify.utils.hiltanotations.Dispatcher
import com.example.shopify.utils.hiltanotations.Dispatchers
import com.example.shopify.utils.response.Response
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
import javax.inject.Inject

@HiltViewModel
class AddressViewModel  @Inject constructor(
    @Dispatcher(Dispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
): ViewModel() {

    private val _state: MutableStateFlow<AddressState> =
        MutableStateFlow(AddressState())
    val state = _state.asStateFlow()


    private val _snackBarFlow: MutableSharedFlow<Int> =
        MutableSharedFlow()
    val snackBarFlow = _snackBarFlow.asSharedFlow()


    fun onEvent(intent: AddressIntent)
    {
        when(intent){
            AddressIntent.MapLoaded -> _state.update { it.copy(loading = false) }
            is AddressIntent.NewLatLong ->{


                _state.update { it.copy(latitude = intent.latitude.toString(), longitude = intent.longitude.toString()) }
            }


        }
    }


}