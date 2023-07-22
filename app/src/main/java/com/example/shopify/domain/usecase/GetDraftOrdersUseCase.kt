package com.example.shopify.domain.usecase

import com.example.shopify.domain.repository.DraftOrdersRepository
import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDraftOrdersUseCase @Inject constructor(
    private val draftOrdersRepository: DraftOrdersRepository
) {

    suspend operator fun <T> invoke(): Flow<Response<T>> = draftOrdersRepository.getDraftOrders()
}