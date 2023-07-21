package com.example.shopify.settings.domain.usecase.account

import android.net.Uri
import com.example.shopify.settings.domain.repository.SettingsRepository
import com.example.shopify.utils.response.Response
import javax.inject.Inject

class UpdateUserImageUseCase @Inject constructor(private val repository : SettingsRepository) {

    suspend fun <T>execute(uri : Uri) : Response<T>
    {
        return repository.updateUserImage(uri)
    }
}