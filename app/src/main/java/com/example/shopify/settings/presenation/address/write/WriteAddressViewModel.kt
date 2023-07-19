package com.example.shopify.settings.presenation.address.write

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopify.R
import com.example.shopify.settings.domain.usecase.dataStore.ReadStringFromDataStoreUseCase
import com.example.shopify.settings.domain.usecase.dataStore.SaveStringToDataStoreUseCase
import com.example.shopify.settings.domain.usecase.location.GetAllCitiesUseCase
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
class WriteAddressViewModel  @Inject constructor(
    @Dispatcher(Dispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    private val getAllCitiesUseCase: GetAllCitiesUseCase,
    private val saveStringToDataStoreUseCase: SaveStringToDataStoreUseCase,
    private val readStringFromDataStoreUseCase: ReadStringFromDataStoreUseCase,
):ViewModel()  {


    private val _state: MutableStateFlow<WriteAddressState> =
        MutableStateFlow(WriteAddressState())
    val state = _state.asStateFlow()


    private val _snackBarFlow: MutableSharedFlow<Int> =
        MutableSharedFlow()
    val snackBarFlow = _snackBarFlow.asSharedFlow()


    fun onEvent(intent: WriteAddressIntent){
        when(intent)
        {
           is  WriteAddressIntent.SaveAddress -> {
                  saveStringToDataStore(intent.key,_state.value.selectedCity+" "+_state.value.address)
            }

            is WriteAddressIntent.NewSelectedCity -> _state.update { it.copy(selectedCity = intent.city) }
            is WriteAddressIntent.NewAddress -> {
                 _state.update { it.copy(address = intent.address) }
            }
        }
    }


    private fun getAllCities()
    {

        viewModelScope.launch(ioDispatcher){
            getAllCitiesUseCase.execute<List<String>>().collectLatest { response ->
                when(response){
                    is Response.Failure -> {
                        _snackBarFlow.emit(R.string.failed_message)
                    }
                    is Response.Loading ->  _state.update { it.copy(loading = true) }
                    is Response.Success -> {
                        _state.update { it.copy(response.data!!,loading = false) }
                    }
                }
            }
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
                    is Response.Success -> {
                        _snackBarFlow.emit(R.string.successfull_message)
                    }
                }
            }
        }
    }


    private fun readStringFromFromDataStore(key: String) {
        viewModelScope.launch(ioDispatcher) {
            readStringFromDataStoreUseCase.execute<String>(key = key).collectLatest { response ->
                when (response) {
                    is Response.Failure -> {
                        _snackBarFlow.emit(R.string.failed_message)
                    }
                    is Response.Loading -> _state.update { it.copy(loading = true) }
                    is Response.Success -> _state.update {
                        it.copy(address = response.data?.split(" ")?.get(1) ?: "", selectedCity = response.data?.split(" ")
                            ?.get(0) ?: "")
                    }
                }
            }
        }
    }



    init {
        getAllCities()
        readStringFromFromDataStore("address")
    }

}