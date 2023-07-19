package com.example.shopify.settings.data.remote

import com.example.shopify.data.remote.RemoteInterface
import com.example.shopify.settings.data.dto.CurrenciesResponse
import com.example.shopify.settings.data.mappers.toCurrenciesModel
import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(private val remoteInterface: RemoteInterface) : RemoteDataSource {

    override suspend fun <T>  getAllCurrencies(): Flow<Response<T>> {
        return flowOf(try {
            Response.Success(remoteInterface.getAllCurrencies().toCurrenciesModel() as T)
        }catch (e : Exception){
          Response.Failure(e.message ?: "unknownError")
        })
    }
}