package com.example.shopify.settings.domain.usecase.location

import com.example.shopify.settings.domain.model.AddressModel
import com.example.shopify.settings.domain.repository.SettingsRepository
import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllAddressesUseCase @Inject constructor(private val repository : SettingsRepository) {

    suspend fun <T> execute() : Flow<List<AddressModel>>
    {
        return repository.getAllAddressFromDatabase()
    }

}