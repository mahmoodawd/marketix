package com.example.shopify.home.domain.repository

import com.example.shopify.home.domain.model.BrandsModel
import com.example.shopify.home.domain.model.ProductsModel
import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    suspend fun getAllBrands(): Flow<Response<BrandsModel>>
    suspend fun  getAllProducts(): Flow<Response<ProductsModel>>

    suspend fun getProductsByBrand(brand: String): Flow<Response<ProductsModel>>

    suspend fun filterProducts(category: Long?, productType: String): Flow<Response<ProductsModel>>
}