package com.example.shopify.productdetails.domain.usecase

import com.example.shopify.productdetails.domain.model.details.ProductsDetailsModel
import com.example.shopify.productdetails.domain.model.details.VariantModel
import com.example.shopify.productdetails.domain.repository.ProductDetailsRepository
import javax.inject.Inject

class AddToCartUseCase @Inject constructor(
    private val productDetailsRepository: ProductDetailsRepository
) {
    suspend operator fun <T> invoke(variantModel: VariantModel?, product: ProductsDetailsModel) =
        productDetailsRepository.createCartDraftOrder<T>(variantModel, product)
}