package com.example.shopify.settings.presenation.address.adresses

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopify.R
import com.example.shopify.settings.domain.model.AddressModel
import com.example.shopify.domain.usecase.dataStore.ReadBooleanDataStoreUseCase
import com.example.shopify.settings.domain.usecase.customer.GetCustomerIdUseCase
import com.example.shopify.settings.domain.usecase.location.DeleteAddressUseCase
import com.example.shopify.settings.domain.usecase.location.GetAllAddressesUseCase
import com.example.shopify.settings.domain.usecase.location.MakeAddressDefaultUseCase
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
import java.util.Collections
import javax.inject.Inject

@HiltViewModel
class AllAddressesViewModel @Inject constructor(
    @Dispatcher(Dispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    private val getAllAddressesUseCase: GetAllAddressesUseCase,
    private val deleteAddressUseCase: DeleteAddressUseCase,
    private val readBooleanFromDataStoreUseCase: ReadBooleanDataStoreUseCase,
    private val getCustomerIdUseCase: GetCustomerIdUseCase,
    private val makeAddressDefaultUseCase: MakeAddressDefaultUseCase
) : ViewModel() {

    private val _state: MutableStateFlow<AllAddressState> =
        MutableStateFlow(AllAddressState())
    val state = _state.asStateFlow()


    private val _snackBarFlow: MutableSharedFlow<Int> =
        MutableSharedFlow()
    val snackBarFlow = _snackBarFlow.asSharedFlow()


    private val _itemsSwapped: MutableSharedFlow<Int> =
        MutableSharedFlow()
    val itemsSwapped = _itemsSwapped.asSharedFlow()


    fun onEvent(intent: AllAddressesIntent) {
        when (intent) {
            is AllAddressesIntent.DeleteAddress -> deleteAddress(intent.position)
            AllAddressesIntent.GetAllAddresses -> getAllAddress()
            is AllAddressesIntent.LatLongFromGPS -> {
                _state.update { it.copy(latitude = intent.latitude, longitude = intent.longitude) }
            }

            AllAddressesIntent.GetUserId -> getCustomerIdWithEmail()
            is AllAddressesIntent.ItemIsDragged -> {
                makeAddressDefault(draggedPosition = intent.draggedPosition, targetPosition = intent.targetPosition)
            }
        }
    }


    private fun getAllAddress() {
        viewModelScope.launch(ioDispatcher) {

            getAllAddressesUseCase.execute<List<AddressModel>>(_state.value.customerId)
                .collectLatest { response ->

                    when (response) {
                        is Response.Failure -> {
                            _snackBarFlow.emit(R.string.failed_message)
                        }
                        is Response.Loading -> {}
                        is Response.Success -> {
                            response.data?.let {
                                _state.update { it.copy(addresses = response.data.sortedBy { address -> !address.isDefault }, loading = false) }
                            }
                        }
                    }

                }
        }
    }

    private fun deleteAddress(position: Int) {
        viewModelScope.launch(ioDispatcher) {
            deleteAddressUseCase.execute<String>(customerId = _state.value.customerId,_state.value.addresses[position]).collectLatest { response ->
                when (response) {
                    is Response.Failure -> {
                        _snackBarFlow.emit(R.string.failed_message)

                    }
                    is Response.Loading -> {}
                    is Response.Success -> {
                            _snackBarFlow.emit(R.string.item_deleted_successfully)
                    }
                }

            }
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

    private fun makeAddressDefault(draggedPosition : Int , targetPosition: Int)
    {
        viewModelScope.launch(ioDispatcher) {
            _state.update { it.copy(loading = true) }
            makeAddressDefaultUseCase.execute<String>(_state.value.customerId,_state.value.addresses[draggedPosition].addressId!!).collectLatest { response ->
                when (response) {
                    is Response.Failure -> {
                        _snackBarFlow.emit(R.string.failed_message)
                        _state.update { it.copy(loading = false) }
                    }
                    is Response.Loading -> {
                        _state.update { it.copy(loading = true) }

                    }
                    is Response.Success ->{
                        Collections.swap(_state.value.addresses,draggedPosition,targetPosition)
                        _state.update { it.copy(loading = false) }
                    }
                }
            }
        }
    }

    private fun getCustomerIdWithEmail()
    {
        viewModelScope.launch(ioDispatcher) {
            getCustomerIdUseCase.execute<String>().collectLatest { response ->
                when (response) {
                    is Response.Failure -> {

                    }

                    is Response.Loading -> _state.update { it.copy(loading = true) }
                    is Response.Success -> _state.update {
                        Log.d("customerId",response.data.toString())
                        it.copy(customerId = response.data!!)
                    }
            }
            }
        }.invokeOnCompletion {
            onEvent(AllAddressesIntent.GetAllAddresses)
        }
    }


    init {
        onEvent(AllAddressesIntent.GetUserId)
        readBooleanPrefFromDataStore("LocationService")
    }


}