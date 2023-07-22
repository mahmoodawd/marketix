package com.example.shopify.home.domain.usecase

import com.example.shopify.home.domain.model.ProductsModel
import com.example.shopify.home.domain.repository.HomeRepository
import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllProductsUseCase @Inject constructor(private val homeRepository: HomeRepository) {
    suspend fun execute(): Flow<Response<ProductsModel>> {
        return homeRepository.getAllProducts()
    }
}