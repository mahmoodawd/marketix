package com.example.shopify.search.data.repository

import com.example.shopify.search.data.dto.SearchProductsResponse
import com.example.shopify.search.data.mappers.toSearchProductsModel
import com.example.shopify.search.data.remote.RemoteDataSource
import com.example.shopify.search.domain.repository.SearchRepository
import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
) : SearchRepository {
    override suspend fun <T> searchProducts(tag: String): Flow<Response<T>> =
        remoteDataSource.searchForProducts<SearchProductsResponse>(tag).map {
            Response.Success(it.data?.toSearchProductsModel() as T)
        }


}

