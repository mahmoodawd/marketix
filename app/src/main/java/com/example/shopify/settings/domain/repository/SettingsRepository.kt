package com.example.shopify.settings.domain.repository

import android.net.Uri
import com.example.shopify.settings.data.dto.location.AddressDto
import com.example.shopify.settings.domain.model.AddressModel
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


    fun getAllAddressFromDatabase() :Flow<List<AddressModel>>

    suspend fun <T> updateAddressInDatabase(addressDto: AddressDto) : Response<T>

    suspend fun <T> insertNewAddressInDatabase(addressDto: AddressDto) : Response<T>

    suspend fun <T> deleteAddressFromDatabase(latitude : Double , longitude : Double) : Response<T>

    suspend fun <T> selectAddressByLatLong(latitude : Double ,longitude : Double)  : Response<T>

}