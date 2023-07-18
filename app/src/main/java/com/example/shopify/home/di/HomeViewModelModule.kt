package com.example.shopify.home.di

import com.example.shopify.home.data.remote.brands.BrandsApiService
import com.example.shopify.home.data.remote.products.ProductsApiService
import com.example.shopify.home.data.repository.HomeRepositoryImp
import com.example.shopify.home.domain.repository.HomeRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(ViewModelComponent::class)
abstract class HomeViewModelModule {

    @Binds
    @ViewModelScoped
    abstract fun bindsBrandsRepository(homeRepositoryImp: HomeRepositoryImp): HomeRepository

    companion object {
        @Provides
        @ViewModelScoped
        fun provideBrandsApiService(): BrandsApiService {
            return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("")
                .build()
                .create(BrandsApiService::class.java)
        }

        @Provides
        @ViewModelScoped
        fun provideProductsApiService(): ProductsApiService {
            return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("")
                .build()
                .create(ProductsApiService::class.java)
        }
    }
}