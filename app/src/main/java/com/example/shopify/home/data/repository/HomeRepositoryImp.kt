package com.example.shopify.home.data.repository

import com.example.shopify.home.data.remote.brands.BrandsApiService
import com.example.shopify.home.data.remote.products.ProductsApiService
import com.example.shopify.home.domain.repository.HomeRepository
import javax.inject.Inject

class HomeRepositoryImp @Inject constructor(private val brandsApiService: BrandsApiService, private val  productsApiService: ProductsApiService) : HomeRepository{
}