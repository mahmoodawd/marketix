package com.example.shopify.domain.usecase

import com.example.shopify.domain.repository.DraftOrdersRepository
import javax.inject.Inject

class RemoveDraftOrderUseCase @Inject constructor(private val draftOrdersRepository: DraftOrdersRepository) {

    suspend operator fun invoke(id: String) = draftOrdersRepository.removeDraftOrder(id)
}