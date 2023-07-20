package com.example.shopify.home.di

import com.example.shopify.home.data.remote.brands.BrandsApiClient
import com.example.shopify.home.data.remote.brands.BrandsRemoteSource
import com.example.shopify.home.data.remote.products.ProductRemoteSource
import com.example.shopify.home.data.remote.products.ProductsApiClient
import com.example.shopify.home.data.repository.HomeRepositoryImp
import com.example.shopify.home.domain.repository.HomeRepository
import com.example.shopify.home.domain.usecase.GetAllBrandsUseCase
import com.example.shopify.home.domain.usecase.GetAllProductsUseCase
import com.example.shopify.home.domain.usecase.GetProductByIdUseCase
import com.example.shopify.home.domain.usecase.GetProductsByCategoryUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class HomeViewModelModule {

    @Binds
    @ViewModelScoped
    abstract fun bindsBrandsRepository(homeRepositoryImp: HomeRepositoryImp): HomeRepository

    @Binds
    @ViewModelScoped
    abstract fun bindsBrandsApiClient(brandsApiClient: BrandsApiClient): BrandsRemoteSource

    @Binds
    @ViewModelScoped
    abstract fun bindsProductsApiClient(productsApiClient: ProductsApiClient): ProductRemoteSource

    companion object {
        @Provides
        @ViewModelScoped
        fun provideGetAllBrandsUseCase(repository: HomeRepository): GetAllBrandsUseCase =
            GetAllBrandsUseCase(repository)

        @Provides
        @ViewModelScoped
        fun provideGetAllProductsUseCase(repository: HomeRepository): GetAllProductsUseCase =
            GetAllProductsUseCase(repository)

        @Provides
        @ViewModelScoped
        fun provideGetProductsByCategoryUseCase(repository: HomeRepository): GetProductsByCategoryUseCase =
            GetProductsByCategoryUseCase(repository)

        @Provides
        @ViewModelScoped
        fun provideGetProductByIdUseCase(repository: HomeRepository): GetProductByIdUseCase =
            GetProductByIdUseCase(repository)
    }
}