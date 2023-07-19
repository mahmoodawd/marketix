package com.example.shopify.settings.domain.usecase.dataStore

import com.example.shopify.settings.domain.repository.SettingsRepository
import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class SaveStringToDataStoreUseCase @Inject constructor(private val repository : SettingsRepository) {

    suspend fun  execute(key : String , value : String) : Flow<Response<String>>
    {
        return  repository.saveStringToDataStore(key, value)
    }
}