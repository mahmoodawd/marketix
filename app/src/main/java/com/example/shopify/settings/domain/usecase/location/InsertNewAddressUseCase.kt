package com.example.shopify.settings.domain.usecase.location

import com.example.shopify.settings.data.mappers.toAddressDto
import com.example.shopify.settings.domain.model.AddressModel
import com.example.shopify.settings.domain.repository.SettingsRepository
import com.example.shopify.utils.response.Response
import javax.inject.Inject

class InsertNewAddressUseCase @Inject constructor(private val repository : SettingsRepository) {
    suspend fun <T> execute(addressModel: AddressModel) : Response<T>
    {
        return  repository.insertNewAddressInDatabase(addressModel.toAddressDto())
    }
}