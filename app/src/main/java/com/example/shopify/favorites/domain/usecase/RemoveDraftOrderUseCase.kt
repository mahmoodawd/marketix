package com.example.shopify.favorites.domain.usecase

import com.example.shopify.favorites.domain.repository.DraftOrdersRepository
import javax.inject.Inject

class RemoveDraftOrderUseCase @Inject constructor(private val draftOrdersRepository: DraftOrdersRepository) {

    suspend operator fun invoke(id: String) = draftOrdersRepository.removeDraftOrder(id)
}