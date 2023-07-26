package com.example.shopify.settings.presenation.address.write

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopify.R
import com.example.shopify.settings.domain.model.AddressModel
import com.example.shopify.settings.domain.usecase.customer.GetCustomerIdUseCase
import com.example.shopify.settings.domain.usecase.location.GetAllCitiesUseCase
import com.example.shopify.settings.domain.usecase.location.InsertNewAddressUseCase
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
    private val getCustomerIdUseCase: GetCustomerIdUseCase
) : ViewModel() {


    private val _state: MutableStateFlow<WriteAddressState> =
        MutableStateFlow(WriteAddressState())
    val state = _state.asStateFlow()


    private val _snackBarFlow: MutableSharedFlow<Int> =
        MutableSharedFlow()
    val snackBarFlow = _snackBarFlow.asSharedFlow()



    private val _addressInserted: MutableSharedFlow<Int> =
        MutableSharedFlow()
    val addressInserted = _addressInserted.asSharedFlow()


    fun onEvent(intent: WriteAddressIntent) {
        when (intent) {
            is WriteAddressIntent.SaveAddress -> {
                if (_state.value.newAddress) {

                    insertNewAddress()
                } else {

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
            WriteAddressIntent.ReadAddressFromDatabase -> {

            }

            WriteAddressIntent.GetUserId -> {getCustomerIdWithEmail()}
        }
    }

    private fun insertNewAddress() {
        viewModelScope.launch(ioDispatcher) {
            val address = AddressModel(
                latitude = _state.value.latitude,
                longitude = _state.value.longitude,
                address = _state.value.address,
                city = _state.value.selectedCity
            )

            _state.update { it.copy(loading =  true) }
            insertNewAddressUseCase.execute<String>(_state.value.customerId,address).collectLatest { response ->
                when (response) {
                    is Response.Failure -> {
                        _snackBarFlow.emit(R.string.failed_message)
                        _state.update { it.copy(loading = false) }
                    }

                    is Response.Loading -> _state.update { it.copy(loading = true) }
                    is Response.Success -> {
                        _state.update { it.copy(loading = false) }
                        _snackBarFlow.emit(R.string.inserted_successfully)
                        _addressInserted.emit(1)
                    }
                }
            }
        }

    }


    private fun getCustomerIdWithEmail() {
        viewModelScope.launch(ioDispatcher) {
            getCustomerIdUseCase.execute<String>().collectLatest { response ->
                when (response) {
                    is Response.Failure -> {
                        _snackBarFlow.emit(R.string.failed_message)
                        _state.update { it.copy(loading = false) }
                    }

                    is Response.Loading -> _state.update { it.copy(loading = true) }
                    is Response.Success -> _state.update {

                        it.copy(customerId = response.data!!, loading = false)
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
                        _state.update { it.copy(cities = response.data!!, loading = false) }
                    }
                }
            }
        }
    }


    init {
        onEvent(WriteAddressIntent.GetUserId)
        getAllCities()
    }

}