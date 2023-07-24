package com.example.shopify.settings.data.local

import com.example.shopify.data.datastore.DataStoreUserPreferences
import com.example.shopify.settings.data.dto.location.AddressDto
import com.example.shopify.settings.data.mappers.toAddressModel
import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val dataStore: DataStoreUserPreferences,
    private val addressDao: AddressDao
) : LocalDataSource {
    override suspend fun <T> saveStringToDataStore(key: String, value: String): Flow<Response<T>> {


        return try {
            dataStore.putString(key, value)
            flowOf(Response.Success("successful insert" as T))
        } catch (e: Exception) {
            flowOf(Response.Failure(e.message ?: "unknown error"))
        }
    }


    override suspend fun <T> getStringFromDataStore(key: String): Flow<Response<T>> {
        return try {
            return dataStore.getString(key)
        } catch (e: Exception) {
            flowOf(Response.Failure(e.message ?: "unknown error"))
        }
    }

    override fun getAllAddressFromDatabase(): Flow<List<AddressDto>> {
        return addressDao.getAllAddress()
    }

    override suspend fun <T> updateAddressInDatabase(addressDto: AddressDto): Response<T> {
        return try {
            val addressesFromDatabase = addressDao.getAddressByLatLong(addressDto.latitude,addressDto.longitude)
           with(addressDto){ addressDao.update(addressesFromDatabase.first().copy(latitude = latitude, longitude = longitude, address = address, city = city))}
            Response.Success("successful update" as T)
        } catch (e: Exception) {
            Response.Failure(e.message ?: "unknown error")
        }
    }


    override suspend fun <T> insertNewAddressInDatabase(addressDto: AddressDto): Response<T> {
        return try {
            addressDao.insert(addressDto)
            Response.Success("successful insert" as T)
        } catch (e: Exception) {
            Response.Failure(e.message ?: "unknown error")
        }
    }

    override suspend fun <T> selectAddressByLatLong(
        latitude: Double,
        longitude: Double
    ): Response<T> {
        return try {
           val address =  addressDao.getAddressByLatLong(latitude, longitude)
            if (address.isNotEmpty()){

            Response.Success(address.first().toAddressModel() as T)
            }else{
                Response.Failure( "new address")
            }
        } catch (e: Exception) {
            Response.Failure(e.message ?: "unknown error")
        }
    }

    override suspend fun <T> deleteAddressFromDatabase(
        latitude: Double,
        longitude: Double
    ): Response<T> {
        return try {
            addressDao.delete(latitude, longitude)
            Response.Success("successful delete" as T)
        } catch (e: Exception) {
            Response.Failure(e.message ?: "unknown error")
        }
    }

    override suspend fun <T> saveBooleanToDataStore(
        key: String,
        value: Boolean
    ): Flow<Response<T>> {
        return try {
            dataStore.putBoolean(key, value)
            flowOf(Response.Success("successful insert" as T))
        } catch (e: Exception) {
            flowOf(Response.Failure(e.message ?: "unknown error"))
        }
    }

    override suspend fun <T> getBooleanFromDataStore(key: String): Flow<Response<T>> {
        return try {
            return dataStore.getBoolean(key)
        } catch (e: Exception) {
            flowOf(Response.Failure(e.message ?: "unknown error"))
        }
    }


}