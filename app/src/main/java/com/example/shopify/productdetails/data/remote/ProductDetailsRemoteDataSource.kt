package com.example.shopify.productdetails.data.remote

import com.example.shopify.data.remote.ShopifyRemoteInterface
import com.example.shopify.productdetails.data.dto.draftorder.DraftOrderRequest
import com.example.shopify.productdetails.data.mappers.toProductsDetailsModel
import com.example.shopify.utils.response.Response
import javax.inject.Inject

class ProductDetailsRemoteDataSource @Inject constructor(private val remoteService: ShopifyRemoteInterface) :

    RemoteDataSource {
    override suspend fun <T> getProductById(id: String) =
        try {
            val productDetails = remoteService.getProductDetailsById(id).product

            Response.Success(productDetails as T)
        } catch (e: Exception) {
            Response.Failure(e.message ?: "unKnownError")
        }

    override suspend fun <T> addDraftOrder(draftOrderRequest: DraftOrderRequest): Response<T> =
        try {
            Response.Success(
                remoteService.createDraftOrder(draftOrderRequest) as T
            )
        } catch (e: Exception) {
            Response.Failure(e.message ?: "unKnownError")
        }

    override suspend fun <T> getDraftOrders(): Response<T> =
        try {
            Response.Success(remoteService.getDraftOrders(fields = "tags,email,line_items") as T)
        } catch (e: Exception) {
            Response.Failure(e.message ?: "unKnownError")
        }


}