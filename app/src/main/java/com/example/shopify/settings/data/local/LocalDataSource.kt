package com.example.shopify.settings.data.local

import com.example.shopify.settings.data.dto.location.AddressDto
import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    suspend fun <T>  saveStringToDataStore(key : String , value : String) : Flow<Response<T>>

    suspend fun <T>  saveBooleanToDataStore(key : String , value : Boolean) : Flow<Response<T>>


    suspend fun <T> getBooleanFromDataStore(key : String) : Flow<Response<T>>

    suspend fun <T> getStringFromDataStore(key : String) : Flow<Response<T>>


     fun getAllAddressFromDatabase() :Flow<List<AddressDto>>

    suspend fun <T> updateAddressInDatabase(addressDto: AddressDto) : Response<T>

    suspend fun <T> insertNewAddressInDatabase(addressDto: AddressDto) : Response<T>

    suspend fun <T> selectAddressByLatLong(latitude : Double ,longitude : Double)  : Response<T>

    suspend fun <T> deleteAddressFromDatabase(latitude : Double , longitude : Double) : Response<T>


}