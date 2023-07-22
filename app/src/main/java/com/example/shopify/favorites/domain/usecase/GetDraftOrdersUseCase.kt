package com.example.shopify.favorites.domain.usecase

import com.example.shopify.domain.repository.DraftOrdersRepository
import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoriteProductsUseCase @Inject constructor(
    private val draftOrdersRepository: DraftOrdersRepository
) {

    suspend operator fun <T> invoke(): Flow<Response<T>> = draftOrdersRepository.getDraftOrders()
}