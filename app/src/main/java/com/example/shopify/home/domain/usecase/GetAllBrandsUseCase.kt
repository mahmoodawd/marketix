package com.example.shopify.home.domain.usecase

import com.example.shopify.home.domain.model.BrandsModel
import com.example.shopify.home.domain.repository.HomeRepository
import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllBrandsUseCase @Inject constructor(private val homeRepository: HomeRepository) {
    suspend fun execute(): Flow<Response<BrandsModel>> {
        return homeRepository.getAllBrands()
    }
}