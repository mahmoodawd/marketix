package com.example.shopify.settings.domain.usecase.account

import com.example.shopify.settings.domain.repository.SettingsRepository
import com.example.shopify.utils.response.Response
import javax.inject.Inject

class UpdateUserPhoneUseCase  @Inject constructor(private val repository : SettingsRepository) {

    suspend fun <T> execute(phone : String) : Response<T>
    {
        return repository.updateUserPhone(phone)
    }
}