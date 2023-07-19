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
    private val readStringFromDataStoreUseCase: ReadStringFromDataStoreUseCase,
    private val saveStringToDataStoreUseCase: SaveStringToDataStoreUseCase
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
            AddressIntent.SaveLastLocation -> saveLatLongToDataStore()
        }
    }


    private fun saveStringToDataStore(key : String ,value : String){
        viewModelScope.launch(ioDispatcher) {
            saveStringToDataStoreUseCase.execute(key,value).collectLatest { response ->

                when(response){
                    is Response.Failure ->{
                        _snackBarFlow.emit(R.string.failed_message)
                    }
                    is Response.Loading -> _state.update { it.copy(loading = true) }
                    is Response.Success -> {}
                }
            }
        }
    }

    private fun saveLatLongToDataStore() {
        viewModelScope.launch(ioDispatcher)
        {
            if (!_state.value.latitude.isNullOrEmpty() && !_state.value.longitude.isNullOrEmpty()) {
                with(_state.value) {
                    saveStringToDataStoreUseCase.execute("latitude", latitude.toString())
                    saveStringToDataStoreUseCase.execute("longitude", longitude.toString())
                }
            }
        }
    }



    private fun readLocationLatLonFromDataStore() {

        viewModelScope.launch(ioDispatcher) {
            readStringFromDataStoreUseCase.execute<String?>("latitude")
                .combine(readStringFromDataStoreUseCase.execute<String?>("longitude")) { latResponse, longResponse ->

                    if (latResponse is Response.Success && longResponse is Response.Success) {



                        latResponse.data?.let {

                        _state.update {
                            it.copy(
                                longitude = longResponse.data,
                                latitude = latResponse.data
                            )
                        }
                            _snackBarFlow.emit(R.string.successfull_message)
                        }
                    }
                }.collect()
        }
    }


    init {
        readLocationLatLonFromDataStore()
    }


}