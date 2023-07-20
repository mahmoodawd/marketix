package com.example.shopify.home.domain.usecase

import com.example.shopify.home.domain.repository.HomeRepository
import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductsByCategoryUseCase @Inject constructor(private val homeRepository: HomeRepository) {
    suspend fun <T> execute(category: Long): Flow<Response<T>> =
        homeRepository.getProductsByCategory(category)

}