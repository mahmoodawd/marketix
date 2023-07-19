package com.example.shopify.settings.domain.usecase.dataStore

import com.example.shopify.settings.domain.repository.SettingsRepository
import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class SaveBooleanToDataStoreUseCase @Inject constructor(private val repository : SettingsRepository) {

    suspend fun  execute(key : String , value : Boolean) : Flow<Response<String>>
    {
        return  repository.saveBooleanToDataStore(key, value)
    }
}