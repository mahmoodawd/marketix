package com.example.shopify.home.data.repository

import com.example.shopify.data.remote.RemoteInterface
import com.example.shopify.home.data.remote.brands.BrandsRemoteSource
import com.example.shopify.home.domain.repository.HomeRepository
import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HomeRepositoryImp @Inject constructor(private val brandsRemoteSource: BrandsRemoteSource) :
    HomeRepository {
    override suspend fun <T> getAllBrands(): Flow<Response<T>> {
        return brandsRemoteSource.getAllBrands()
    }
}