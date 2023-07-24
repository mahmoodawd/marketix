package com.example.shopify.productdetails.domain.repository

import com.example.shopify.productdetails.domain.model.ProductsDetailsModel
import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow

interface ProductDetailsRepository {
    suspend fun <T> getProductById(id: String): Flow<Response<T>>

    suspend fun <T> createFavoriteDraftOrder(productsDetailsModel: ProductsDetailsModel): Flow<Response<T>>
    suspend fun <T> createCartDraftOrder(productsDetailsModel: ProductsDetailsModel): Flow<Response<T>>
}