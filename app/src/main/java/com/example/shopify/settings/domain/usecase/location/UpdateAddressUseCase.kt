package com.example.shopify.settings.domain.usecase.location

import com.example.shopify.settings.data.mappers.toAddressDto
import com.example.shopify.settings.domain.model.AddressModel
import com.example.shopify.settings.domain.repository.SettingsRepository
import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateAddressUseCase @Inject constructor(private val repository : SettingsRepository) {

    suspend fun <T> execute(addressModel: AddressModel) : Response<T>
    {
        return repository.updateAddressInDatabase(addressModel.toAddressDto())
    }


}