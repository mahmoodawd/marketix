package com.example.shopify.home.domain.usecase

import com.example.shopify.home.domain.repository.HomeRepository
import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllProductsUseCase @Inject constructor(private val homeRepository: HomeRepository) {
    suspend fun  <T> execute(brand : String): Flow<Response<T>> {
        return homeRepository.getAllProducts(brand)
    }
}