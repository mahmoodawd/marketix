package com.example.shopify.orders.data.repository

import android.net.Uri
import com.example.shopify.settings.data.dto.address.SendAddressDTO
import com.example.shopify.settings.domain.repository.SettingsRepository
import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeSettingsRepository(
    private val currency: String,
    private val currencyFactor: String,
) : SettingsRepository {
    override suspend fun <T> saveStringToDataStore(key: String, value: String): Flow<Response<T>> {
        TODO("Not yet implemented")
    }

    override suspend fun <T> getStringFromDataStore(key: String): Flow<Response<T>> {
        return flowOf(

            if (key == "currency") {
                Response.Success(currency as T)
            } else {
                Response.Success(currencyFactor as T)
            }
        )
    }

    override suspend fun <T> saveBooleanToDataStore(
        key: String,
        value: Boolean
    ): Flow<Response<T>> {
        TODO("Not yet implemented")
    }

    override suspend fun <T> getBooleanFromDataStore(key: String): Flow<Response<T>> {
        TODO("Not yet implemented")
    }

    override suspend fun <T> getAllCurrencies(): Flow<Response<T>> {
        TODO("Not yet implemented")
    }

    override suspend fun <T> getAllCities(): Flow<Response<T>> {
        TODO("Not yet implemented")
    }

    override suspend fun <T> updateUserImage(uri: Uri): Response<T> {
        TODO("Not yet implemented")
    }

    override suspend fun <T> updateUsername(userName: String): Response<T> {
        TODO("Not yet implemented")
    }

    override suspend fun <T> updateUserPhone(phone: String): Response<T> {
        TODO("Not yet implemented")
    }

    override suspend fun <T> getUserName(): Response<T> {
        TODO("Not yet implemented")
    }

    override suspend fun <T> getUserImage(): Response<T> {
        TODO("Not yet implemented")
    }

    override suspend fun <T> getUserPhone(): Response<T> {
        TODO("Not yet implemented")
    }

    override suspend fun <T> getAllCustomerAddress(customerId: String): Flow<Response<T>> {
        TODO("Not yet implemented")
    }

    override suspend fun <T> createAddressForCustomer(
        customerId: String,
        customerAddress: SendAddressDTO
    ): Flow<Response<T>> {
        TODO("Not yet implemented")
    }

    override suspend fun <T> deleteAddressForCustomer(
        customerId: String,
        addressId: String
    ): Flow<Response<T>> {
        TODO("Not yet implemented")
    }

    override suspend fun <T> makeAddressDefaultForCustomer(
        customerId: String,
        addressId: String
    ): Flow<Response<T>> {
        TODO("Not yet implemented")
    }
}