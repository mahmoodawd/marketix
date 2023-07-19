package com.example.shopify.settings.domain.usecase

import com.example.shopify.settings.domain.repository.SettingsRepository
import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllCurrenciesUseCase @Inject constructor(private val settingsRepository: SettingsRepository) {

    suspend fun  <T> execute() : Flow<Response<T>>
    {
        return  settingsRepository.getAllCurrencies<T>()
    }
}