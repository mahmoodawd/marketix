package com.example.shopify.settings.domain.usecase.account

import com.example.shopify.settings.domain.repository.SettingsRepository
import com.example.shopify.utils.response.Response
import javax.inject.Inject

class UpdateUserNameUseCase @Inject constructor(private val repository : SettingsRepository) {


    suspend fun <T> execute(userName : String) : Response<T>
    {
        return repository.updateUsername(userName)
    }
}