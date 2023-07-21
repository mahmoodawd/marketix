package com.example.shopify.settings.presenation.address.write

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopify.R
import com.example.shopify.settings.domain.model.AddressModel
import com.example.shopify.settings.domain.usecase.dataStore.ReadStringFromDataStoreUseCase
import com.example.shopify.settings.domain.usecase.dataStore.SaveStringToDataStoreUseCase
import com.example.shopify.settings.domain.usecase.location.GetAllCitiesUseCase
import com.example.shopify.settings.domain.usecase.location.InsertNewAddressUseCase
import com.example.shopify.settings.domain.usecase.location.SelectAddressByLatLongUseCase
import com.example.shopify.settings.domain.usecase.location.UpdateAddressUseCase
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
class WriteAddressViewModel @Inject constructor(
    @Dispatcher(Dispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    private val getAllCitiesUseCase: GetAllCitiesUseCase,
    private val insertNewAddressUseCase: InsertNewAddressUseCase,
    private val selectAddressByLatLongUseCase: SelectAddressByLatLongUseCase,
    private val updateAddressUseCase: UpdateAddressUseCase
) : ViewModel() {


    private val _state: MutableStateFlow<WriteAddressState> =
        MutableStateFlow(WriteAddressState())
    val state = _state.asStateFlow()


    private val _snackBarFlow: MutableSharedFlow<Int> =
        MutableSharedFlow()
    val snackBarFlow = _snackBarFlow.asSharedFlow()


    fun onEvent(intent: WriteAddressIntent) {
        when (intent) {
            is WriteAddressIntent.SaveAddress -> {
                if (_state.value.newAddress) {

                    insertNewAddressToDataBase()
                } else {
                    updateAddress()
                }
            }

            is WriteAddressIntent.NewSelectedCity -> _state.update { it.copy(selectedCity = intent.city) }
            is WriteAddressIntent.NewAddress -> {
                _state.update { it.copy(address = intent.address) }
            }

            is WriteAddressIntent.NewLatLong -> {
                _state.update { it.copy(latitude = intent.latitude, longitude = intent.longitude) }
                onEvent(WriteAddressIntent.ReadAddressFromDatabase)
            }
            WriteAddressIntent.ReadAddressFromDatabase -> getAddressByLatLong()
        }
    }

    private fun insertNewAddressToDataBase() {
        viewModelScope.launch(ioDispatcher) {
            val address = AddressModel(
                latitude = _state.value.latitude,
                longitude = _state.value.longitude,
                address = _state.value.address,
                city = _state.value.selectedCity
            )
            val insertResult = insertNewAddressUseCase.execute<String>(address)
            if (insertResult is Response.Success) {
                _snackBarFlow.emit(R.string.inserted_successfully)
            } else {
                _snackBarFlow.emit(R.string.failed_message)

            }

        }

    }


    private fun updateAddress() {
        viewModelScope.launch(ioDispatcher) {
            with(_state.value) {
                val updateResult = updateAddressUseCase.execute<String>(
                    AddressModel(
                        latitude = latitude,
                        longitude = longitude,
                        city = selectedCity,
                        address = address
                    )
                )

                if (updateResult is Response.Success){
                    _snackBarFlow.emit(R.string.account_updated_successfully)
                }
            }
        }
    }


    private fun getAddressByLatLong() {

        with(_state.value) {
            viewModelScope.launch(ioDispatcher) {
                val addressResponse =
                    selectAddressByLatLongUseCase.execute<AddressModel>(latitude, longitude)
                if (addressResponse is Response.Success) {
                    _state.update {
                        it.copy(
                            address = addressResponse.data!!.address,
                            selectedCity = addressResponse.data.city,
                            latitude = addressResponse.data.latitude,
                            longitude = addressResponse.data.longitude,
                            newAddress = false
                        )
                    }
                } else {
                    _state.update {
                        it.copy(
                            newAddress = true
                        )
                    }
                }
            }

        }


    }


    private fun getAllCities() {

        viewModelScope.launch(ioDispatcher) {
            getAllCitiesUseCase.execute<List<String>>().collectLatest { response ->
                when (response) {
                    is Response.Failure -> {
                        _snackBarFlow.emit(R.string.failed_message)
                    }

                    is Response.Loading -> _state.update { it.copy(loading = true) }
                    is Response.Success -> {
                        _state.update { it.copy(response.data!!, loading = false) }
                    }
                }
            }
        }
    }


    init {
        getAllCities()
    }

}