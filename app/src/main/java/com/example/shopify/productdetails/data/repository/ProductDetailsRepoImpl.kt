package com.example.shopify.productdetails.data.repository

import com.example.shopify.data.dto.DraftOrderResponse
import com.example.shopify.productdetails.data.dto.productdetails.Product
import com.example.shopify.productdetails.data.dto.productdetails.ProductDetailsResponse
import com.example.shopify.productdetails.data.mappers.toFavoriteDraftOrderRequest
import com.example.shopify.productdetails.data.mappers.toProductsDetailsModel
import com.example.shopify.productdetails.data.remote.RemoteDataSource
import com.example.shopify.productdetails.domain.model.ProductsDetailsModel
import com.example.shopify.productdetails.domain.repository.ProductDetailsRepository
import com.example.shopify.utils.constants.TAG_CART
import com.example.shopify.utils.constants.TAG_FAVORITES
import com.example.shopify.utils.response.Response
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class ProductDetailsRepoImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val auth: FirebaseAuth
) :
    ProductDetailsRepository {
    override suspend fun <T> getProductById(id: String): Flow<Response<T>> =
        flowOf(
            Response.Success(
                remoteDataSource.getProductById<Product>(id).data?.toProductsDetailsModel() as T
            )


        )

    override suspend
    fun <T> createFavoriteDraftOrder(productsDetailsModel: ProductsDetailsModel): Flow<Response<T>> {

        //Handle if the item is already added so ignore it
        val draftOrders =
            remoteDataSource.getDraftOrders<DraftOrderResponse>().data!!.draft_orders
        val itemExist = draftOrders.any {
            it.tags == TAG_FAVORITES &&
                    it.email == auth.currentUser?.email &&
                    it.line_items.first().variant_id == productsDetailsModel.variants?.first()?.id
        }

        return flowOf(
            if (itemExist) {

                Response.Failure("itemAlreadyExistException")

            } else {
                remoteDataSource.addDraftOrder(productsDetailsModel.toFavoriteDraftOrderRequest())

            }
        )
    }

    override suspend
    fun <T> createCartDraftOrder(productsDetailsModel: ProductsDetailsModel): Flow<Response<T>> {
        //Handle if the item is already added so ignore it
        val draftOrders =
            remoteDataSource.getDraftOrders<DraftOrderResponse>().data!!.draft_orders
        val itemExist = draftOrders.any {
            it.tags == TAG_CART &&
                    it.email == auth.currentUser?.email &&
                    it.line_items.first().variant_id == productsDetailsModel.variants?.first()?.id
        }

        return flowOf(
            if (itemExist) {

                Response.Failure("itemAlreadyExistException")

            } else {
                remoteDataSource.addDraftOrder(productsDetailsModel.toFavoriteDraftOrderRequest())

            }
        )
    }
}

