package com.example.shopify.home.data.repository

import com.example.shopify.data.dto.codes.DiscountCode
import com.example.shopify.data.dto.codes.DiscountCodesResponse
import com.example.shopify.home.data.dto.BrandsResponse
import com.example.shopify.home.data.dto.ProductsResponse
import com.example.shopify.home.data.local.HomeLocalDataSource
import com.example.shopify.home.data.mappers.toBrandsModel
import com.example.shopify.home.data.mappers.toDiscountCode
import com.example.shopify.home.data.mappers.toDiscountCodeModel
import com.example.shopify.home.data.mappers.toProductsModel
import com.example.shopify.home.data.remote.brands.BrandsRemoteSource
import com.example.shopify.home.data.remote.products.ProductRemoteSource
import com.example.shopify.home.domain.model.BrandsModel
import com.example.shopify.home.domain.model.ProductsModel
import com.example.shopify.home.domain.model.discountcode.DiscountCodeModel
import com.example.shopify.home.domain.repository.HomeRepository
import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HomeRepositoryImp @Inject constructor(
    private val brandsRemoteSource: BrandsRemoteSource,
    private val productRemoteSource: ProductRemoteSource,
    private val homeLocalDataSource: HomeLocalDataSource
) :
    HomeRepository {
    override suspend fun getAllBrands(): Flow<Response<BrandsModel>> {
        return try {
            brandsRemoteSource.getAllBrands<BrandsResponse>().map {
                Response.Success(it.data!!.toBrandsModel())
            }
        } catch (e: Exception) {
            flowOf(Response.Failure(e.message ?: "UnKnown"))
        }

    }


    override suspend fun getAllProducts(): Flow<Response<ProductsModel>> {
        return try {
            productRemoteSource.getAllProducts<ProductsResponse>().map {
                Response.Success(it.data!!.toProductsModel())
            }
        } catch (e: Exception) {
            flowOf(Response.Failure(e.message ?: "Unknown"))
        }
    }


    override suspend fun getProductsByBrand(brand: String): Flow<Response<ProductsModel>> {
        return try {
            productRemoteSource.getProductsByBrand<ProductsResponse>(brand).map {
                Response.Success(it.data!!.toProductsModel())
            }
        } catch (e: Exception) {
            flowOf(Response.Failure(e.message ?: "UnKnown"))
        }

    }


    override suspend fun filterProducts(
        category: Long?,
        productType: String
    ): Flow<Response<ProductsModel>> {
        return try {
            productRemoteSource.filterProducts<ProductsResponse>(category, productType).map {
                Response.Success(it.data!!.toProductsModel())
            }
        } catch (e: Exception) {
            flowOf(Response.Failure(e.message ?: "UnKnown"))
        }
    }

    override suspend fun <T> getDiscountCodes(): Flow<Response<T>> {
        return productRemoteSource.getDiscountCodes<T>().map { response ->
            Response.Success((response.data as DiscountCodesResponse).toDiscountCodeModel() as T)
        }
    }

    override suspend fun <T> insertDiscountCodeToDatabase(code: DiscountCodeModel): Flow<Response<T>> {
        return homeLocalDataSource.insertDiscountCodeToDatabase(code.toDiscountCode())
    }
}