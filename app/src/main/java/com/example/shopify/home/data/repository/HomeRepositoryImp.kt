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

    override suspend fun <T> getAllProducts(): Flow<Response<T>> =
        productRemoteSource.getAllProducts()

    override suspend fun <T> getProductsByBrand(brand: String): Flow<Response<T>> =
        productRemoteSource.getProductsByBrand(brand)

    override suspend fun <T> filterProducts(
        category: Long?,
        productType: String
    ): Flow<Response<T>> =
        productRemoteSource.filterProducts(category, productType)


}