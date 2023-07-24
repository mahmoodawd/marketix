package com.example.shopify.productdetails.domain.usecase

import com.example.shopify.productdetails.domain.model.details.ProductsDetailsModel
import com.example.shopify.productdetails.domain.repository.ProductDetailsRepository
import javax.inject.Inject

class AddToCartUseCase @Inject constructor(
    private val productDetailsRepository: ProductDetailsRepository
) {
    suspend operator fun <T> invoke(productsDetailsModel: ProductsDetailsModel) =
        productDetailsRepository.createCartDraftOrder<T>(productsDetailsModel)
}