package com.example.shopify.favorites.domain.usecase

import com.example.shopify.favorites.domain.repository.FavoritesRepository
import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoriteProductsUseCase @Inject constructor(
    private val favoritesRepository: FavoritesRepository
) {

    suspend operator fun <T> invoke(): Flow<Response<T>> = favoritesRepository.getDraftOrders()
}