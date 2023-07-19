package com.example.shopify.settings.data.remote

import com.example.shopify.data.remote.RemoteInterface
import com.example.shopify.settings.data.dto.location.CitiesPostRequest
import com.example.shopify.settings.data.mappers.toCities
import com.example.shopify.settings.data.mappers.toCurrenciesModel
import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(private val remoteInterface: RemoteInterface,private val remoteCountriesInterface: RemoteCountriesInterface) : RemoteDataSource {

    override suspend fun <T>  getAllCurrencies(): Flow<Response<T>> {
        return flowOf(try {
            Response.Success(remoteInterface.getAllCurrencies().toCurrenciesModel() as T)
        }catch (e : Exception){
          Response.Failure(e.message ?: "unknownError")
        })
    }

    override suspend fun <T> getAllCities(): Flow<Response<T>> {
        return flowOf(try {
            Response.Success(remoteCountriesInterface.getAllCities(CitiesPostRequest()).toCities() as T)
        }catch (e : Exception){
            Response.Failure(e.message ?: "unknownError")
        })
    }
}