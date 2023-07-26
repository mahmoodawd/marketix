package com.example.shopify.settings.domain.repository

import android.net.Uri
import com.example.shopify.settings.data.dto.address.SendAddressDTO
import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    suspend fun <T>  saveStringToDataStore(key : String , value : String) : Flow<Response<T>>


    suspend fun <T> getStringFromDataStore(key : String) : Flow<Response<T>>


    suspend fun <T>  saveBooleanToDataStore(key : String , value : Boolean) : Flow<Response<T>>


    suspend fun <T> getBooleanFromDataStore(key : String) : Flow<Response<T>>



    suspend fun <T> getAllCurrencies() : Flow<Response<T>>


    suspend fun <T> getAllCities() :  Flow<Response<T>>


    suspend fun  <T>updateUserImage(uri : Uri) : Response<T>
    suspend fun  <T>updateUsername(userName : String) : Response<T>
    suspend fun  <T>updateUserPhone(phone : String) : Response<T>


    suspend fun <T> getUserName() : Response<T>

    suspend fun <T> getUserImage() : Response<T>

    suspend fun <T> getUserPhone() : Response<T>


    suspend fun <T> getAllCustomerAddress(customerId : String) : Flow<Response<T>>

    suspend fun <T> createAddressForCustomer(customerId: String,customerAddress: SendAddressDTO) : Flow<Response<T>>

    suspend fun <T> deleteAddressForCustomer(customerId: String , addressId : String) : Flow<Response<T>>






}