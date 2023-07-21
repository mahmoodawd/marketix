package com.example.shopify.settings.domain.usecase.account

import com.example.shopify.settings.domain.repository.SettingsRepository
import com.example.shopify.utils.response.Response
import javax.inject.Inject

class GetUserImageUseCase @Inject constructor(private val repository : SettingsRepository) {

    suspend fun <T>execute() : Response<T>
    {
        return repository.getUserImage()
    }
}