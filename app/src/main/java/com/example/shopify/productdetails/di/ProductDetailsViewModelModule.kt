package com.example.shopify.productdetails.di

import com.example.shopify.productdetails.data.remote.ProductDetailsRemoteDataSource
import com.example.shopify.productdetails.data.remote.RemoteDataSource
import com.example.shopify.productdetails.data.repository.ProductDetailsRepoImpl
import com.example.shopify.productdetails.domain.repository.ProductDetailsRepository
import com.example.shopify.productdetails.domain.usecase.AddToCartUseCase
import com.example.shopify.productdetails.domain.usecase.AddToFavoritesUseCase
import com.example.shopify.productdetails.domain.usecase.GetProductDetailsUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class ProductDetailsViewModelModule {


    @Binds
    @ViewModelScoped
    abstract fun bindsProductDetailsRemoteDataSource(productDetailsRemoteDataSource: ProductDetailsRemoteDataSource): RemoteDataSource

    @Binds
    @ViewModelScoped
    abstract fun bindsProductDetailsRepo(productDetailsRepository: ProductDetailsRepoImpl): ProductDetailsRepository

    companion object {
        @Provides
        @ViewModelScoped
        fun provideGetProductDetailsUseCase(productDetailsRepository: ProductDetailsRepository) =
            GetProductDetailsUseCase(productDetailsRepository)

        @Provides
        @ViewModelScoped
        fun provideAddToCartUseCase(productDetailsRepository: ProductDetailsRepository) =
            AddToCartUseCase(productDetailsRepository)

        @Provides
        @ViewModelScoped
        fun provideAddToFavoriteUseCase(productDetailsRepository: ProductDetailsRepository) =
            AddToFavoritesUseCase(productDetailsRepository)


    }


}