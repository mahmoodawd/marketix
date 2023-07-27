package com.example.shopify.search.domain.usecase

import com.example.shopify.search.domain.repository.SearchRepository
import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSearchResultUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {

    suspend operator fun <T> invoke(tag: String): Flow<Response<T>> =
        searchRepository.searchProducts<T>(tag)

}