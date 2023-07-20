package com.example.shopify.settings.presenation.address.adresses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopify.settings.domain.model.AddressModel
import com.example.shopify.settings.domain.usecase.location.DeleteAddressUseCase
import com.example.shopify.settings.domain.usecase.location.GetAllAddressesUseCase
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
class AllAddressesViewModel @Inject constructor(
    @Dispatcher(Dispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    private val getAllAddressesUseCase: GetAllAddressesUseCase,
    private val deleteAddressUseCase: DeleteAddressUseCase,
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


    init {
        onEvent(AllAddressesIntent.GetAllAddresses)
    }



}