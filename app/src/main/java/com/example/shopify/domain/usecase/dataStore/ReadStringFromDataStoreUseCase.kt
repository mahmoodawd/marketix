package com.example.shopify.domain.usecase.dataStore

import com.example.shopify.settings.domain.repository.SettingsRepository
import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReadStringFromDataStoreUseCase @Inject constructor(private val repository : SettingsRepository) {

    suspend fun  <T> execute(key : String) : Flow<Response<T>>
    {
            return repository.getStringFromDataStore(key)
    }

}