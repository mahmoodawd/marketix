package com.example.shopify.home.data.repository

import com.example.shopify.home.data.remote.brands.BrandsRemoteSource
import com.example.shopify.home.data.remote.products.ProductRemoteSource
import com.example.shopify.home.domain.repository.HomeRepository
import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HomeRepositoryImp @Inject constructor(
    private val brandsRemoteSource: BrandsRemoteSource,
    private val productRemoteSource: ProductRemoteSource
) :
    HomeRepository {
    override suspend fun <T> getAllBrands(): Flow<Response<T>> =
        brandsRemoteSource.getAllBrands()

    override suspend fun <T> getAllProducts(brand: String,id : Long): Flow<Response<T>> =
        productRemoteSource.getAllProducts(brand, id)

    override suspend fun <T> getProductsByCategory(category: Long): Flow<Response<T>> =
        productRemoteSource.getProductsByCategory(category)

}