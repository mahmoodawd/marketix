package com.example.shopify.settings.presenation.address.adresses

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopify.R
import com.example.shopify.settings.domain.model.AddressModel
import com.example.shopify.domain.usecase.dataStore.ReadBooleanDataStoreUseCase
import com.example.shopify.settings.domain.usecase.location.DeleteAddressUseCase
import com.example.shopify.settings.domain.usecase.location.GetAllAddressesUseCase
import com.example.shopify.settings.domain.usecase.location.UpdateAddressUseCase
import com.example.shopify.settings.presenation.settings.SettingsState
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
import javax.inject.Inject

@HiltViewModel
class AllAddressesViewModel @Inject constructor(
    @Dispatcher(Dispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    private val getAllAddressesUseCase: GetAllAddressesUseCase,
    private val deleteAddressUseCase: DeleteAddressUseCase,
    private val readBooleanFromDataStoreUseCase: ReadBooleanDataStoreUseCase,
): ViewModel() {

    private val _state: MutableStateFlow<AllAddressState> =
        MutableStateFlow(AllAddressState())
    val state = _state.asStateFlow()


    private val _snackBarFlow: MutableSharedFlow<Int> =
        MutableSharedFlow()
    val snackBarFlow = _snackBarFlow.asSharedFlow()


    fun onEvent(intent : AllAddressesIntent)
    {
        when(intent)
        {
            is AllAddressesIntent.DeleteAddress -> deleteAddress(intent.position)
            AllAddressesIntent.GetAllAddresses -> getAllAddress()
            is AllAddressesIntent.LatLongFromGPS ->{

                 Log.d("newLatLong",intent.latitude.toString())
            _state.update { it.copy(latitude = intent.latitude, longitude = intent.longitude) }
            }
        }
    }


   private fun getAllAddress()
    {
        viewModelScope.launch(ioDispatcher){
            getAllAddressesUseCase.execute<AddressModel>().collectLatest { response ->
                     _state.update { it.copy(addresses =  response) }
            }
        }
    }

    private  fun deleteAddress(position : Int)
    {
        viewModelScope.launch(ioDispatcher){
            deleteAddressUseCase.execute<String>(addressModel = _state.value.addresses[position])
        }
    }


    private fun readBooleanPrefFromDataStore(key: String) {
        val property = AllAddressState::class.java.getDeclaredField(key)
        property.isAccessible = true
        viewModelScope.launch(ioDispatcher) {
            readBooleanFromDataStoreUseCase.execute<Boolean>(key = key).collectLatest { response ->
                when (response) {
                    is Response.Failure -> {
                        _snackBarFlow.emit(R.string.failed_message)
                    }
                    is Response.Loading -> _state.update { it.copy(loading = true) }
                    is Response.Success -> _state.update {
                        val newState = _state.value.copy()
                        property.set(newState, response.data ?: true)
                        _state.update { newState }
                        it.copy()
                    }
                }
            }
        }
    }


    init {
        onEvent(AllAddressesIntent.GetAllAddresses)
        readBooleanPrefFromDataStore("LocationService")
    }



}