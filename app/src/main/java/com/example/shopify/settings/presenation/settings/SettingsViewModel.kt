package com.example.shopify.settings.presenation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopify.R
import com.example.shopify.settings.domain.model.CurrenciesModel
import com.example.shopify.settings.domain.model.CurrencyModel
import com.example.shopify.settings.domain.usecase.GetAllCurrenciesUseCase
import com.example.shopify.settings.domain.usecase.dataStore.ReadBooleanDataStoreUseCase
import com.example.shopify.settings.domain.usecase.dataStore.ReadStringFromDataStoreUseCase
import com.example.shopify.settings.domain.usecase.dataStore.SaveBooleanToDataStoreUseCase
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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SettingsViewModel @Inject constructor(
    @Dispatcher(Dispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    private val getAllCurrenciesUseCase: GetAllCurrenciesUseCase,
    private val saveBooleanToDataStoreUseCase: SaveBooleanToDataStoreUseCase,
    private val readBooleanFromDataStoreUseCase: ReadBooleanDataStoreUseCase,
    private val saveStringToDataStoreUseCase: SaveStringToDataStoreUseCase,
    private val readStringFromDataStoreUseCase: ReadStringFromDataStoreUseCase,
) : ViewModel() {

    private val _state: MutableStateFlow<SettingsState> =
        MutableStateFlow(SettingsState())
    val state = _state.asStateFlow()



    private val _snackBarFlow: MutableSharedFlow<Int> =
        MutableSharedFlow()
    val snackBarFlow = _snackBarFlow.asSharedFlow()


     fun onEvent(intent: SettingsIntent) {
        when (intent) {
            SettingsIntent.GetCurrencies -> getAllCurrencies()
            is SettingsIntent.SaveLocationPref ->  saveBooleanToDataStore(intent.key,intent.value)
            is SettingsIntent.SaveNotificationPref -> saveBooleanToDataStore(intent.key,intent.value)
            is SettingsIntent.SaveCurrencyPref -> saveStringToDataStore(intent.key,intent.value)
        }
    }

    private fun getAllCurrencies() {
        viewModelScope.launch(ioDispatcher) {

            getAllCurrenciesUseCase.execute<CurrenciesModel>().collectLatest { response ->
                when (response) {
                    is Response.Failure -> {
                        _snackBarFlow.emit(R.string.failed_message)
                    }
                    is Response.Loading -> _state.update { it.copy(loading = true) }
                    is Response.Success -> {

                        val currenciesList = mutableListOf<CurrencyModel>()
                        currenciesList.add(0, CurrencyModel("EGP"))
                        currenciesList.addAll(response.data?.currencies!!)
                        _state.update {
                            it.copy(currencies = currenciesList)
                        }
                    }
                }
            }
        }
    }

    private fun readBooleanPrefFromDataStore(key: String) {
        val property = SettingsState::class.java.getDeclaredField(key)
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



    private fun readStringPrefFromDataStore(key: String) {
        viewModelScope.launch(ioDispatcher) {
            readStringFromDataStoreUseCase.execute<String>(key = key).collectLatest { response ->
                when (response) {
                    is Response.Failure -> {
                        _snackBarFlow.emit(R.string.failed_message)
                    }
                    is Response.Loading -> _state.update { it.copy(loading = true) }
                    is Response.Success -> _state.update {
                        it.copy(selectedCurrency = response.data ?: "EGP")
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
                    is Response.Success -> {}
                }
            }
        }
    }

    private fun saveBooleanToDataStore(key : String ,value : Boolean){
        viewModelScope.launch(ioDispatcher) {
            saveBooleanToDataStoreUseCase.execute(key,value).collectLatest { response ->

                when(response){
                    is Response.Failure ->{
                        _snackBarFlow.emit(R.string.failed_message)
                    }
                    is Response.Loading -> _state.update { it.copy(loading = true) }
                    is Response.Success -> {

                    }
                }
            }
        }
    }


    init {
        onEvent(SettingsIntent.GetCurrencies)
        readBooleanPrefFromDataStore("notification")
        readBooleanPrefFromDataStore("LocationService")
        readStringPrefFromDataStore("currency")
    }
}