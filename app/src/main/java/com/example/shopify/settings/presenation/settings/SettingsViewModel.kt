package com.example.shopify.settings.presenation.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopify.settings.domain.model.CurrenciesModel
import com.example.shopify.settings.domain.usecase.GetAllCurrenciesUseCase
import com.example.shopify.utils.hiltanotations.Dispatcher
import com.example.shopify.utils.hiltanotations.Dispatchers
import com.example.shopify.utils.response.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class SettingsViewModel @Inject constructor(
    @Dispatcher(Dispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    private val getAllCurrenciesUseCase: GetAllCurrenciesUseCase
) : ViewModel() {


     fun getAllCurrencies() {
                viewModelScope.launch(ioDispatcher){

                    getAllCurrenciesUseCase.execute<CurrenciesModel>().collectLatest { response ->
                        when (response) {
                            is Response.Failure -> {
                                Timber.tag("currencies")
                                    .d(response.error)
                            }
                            is Response.Loading -> TODO()
                            is Response.Success -> Timber.tag("currencies")
                                .d(response.data?.currencies.toString())
                        }
                    }
                }
    }


    init {
        getAllCurrencies()
    }
}