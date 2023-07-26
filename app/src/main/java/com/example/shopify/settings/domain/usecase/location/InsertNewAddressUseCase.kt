package com.example.shopify.settings.domain.usecase.location

import com.example.shopify.settings.data.mappers.toSendAddressDto
import com.example.shopify.settings.domain.model.AddressModel
import com.example.shopify.settings.domain.repository.SettingsRepository
import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class InsertNewAddressUseCase @Inject constructor(private val repository : SettingsRepository) {
    suspend fun <T> execute(customerId : String,addressModel: AddressModel) : Flow<Response<T>>
    {
        return  repository.createAddressForCustomer(customerId,addressModel.toSendAddressDto())
    }
}