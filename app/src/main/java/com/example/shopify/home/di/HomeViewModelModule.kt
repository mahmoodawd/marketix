package com.example.shopify.home.di

import com.example.shopify.home.data.remote.brands.BrandsApiClient
import com.example.shopify.home.data.remote.brands.BrandsRemoteSource
import com.example.shopify.home.data.repository.HomeRepositoryImp
import com.example.shopify.home.domain.repository.HomeRepository
import com.example.shopify.home.domain.usecase.GetAllBrandsUseCase
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

    companion object {
        @Provides
        @ViewModelScoped
        fun provideGetAllBrandsUseCase(repository: HomeRepository): GetAllBrandsUseCase =
            GetAllBrandsUseCase(repository)
    }
}