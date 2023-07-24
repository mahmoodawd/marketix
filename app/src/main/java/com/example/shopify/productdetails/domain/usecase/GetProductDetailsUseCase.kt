package com.example.shopify.productdetails.domain.usecase

import com.example.shopify.productdetails.domain.repository.ProductDetailsRepository
import javax.inject.Inject

class GetProductDetailsUseCase @Inject constructor(
    private val productDetailsRepository: ProductDetailsRepository
) {
    suspend operator fun <T> invoke(id: String) =
        productDetailsRepository.getProductById<T>(id)
}