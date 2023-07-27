package com.example.shopify.search.di

import com.example.shopify.search.data.local.LocalDataSource
import com.example.shopify.search.data.local.SearchLocalDataSource
import com.example.shopify.search.data.remote.RemoteDataSource
import com.example.shopify.search.data.remote.SearchRemoteDataSource
import com.example.shopify.search.data.repository.SearchRepositoryImpl
import com.example.shopify.search.domain.repository.SearchRepository
import com.example.shopify.search.domain.usecase.GetSearchResultUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class SearchViewModelModule {


    @Binds
    @ViewModelScoped
    abstract fun bindSearchDataSource(searchRemoteDataSource: SearchRemoteDataSource): RemoteDataSource

    @Binds
    @ViewModelScoped
    abstract fun bindSearchLocalDataSource(searchLocalDataSource: SearchLocalDataSource): LocalDataSource

    @Binds
    @ViewModelScoped
    abstract fun bindSearchRepo(searchRepositoryImpl: SearchRepositoryImpl): SearchRepository

    companion object {
        @Provides
        @ViewModelScoped
        fun provideGetSearchResultUseCase(searchRepository: SearchRepository) =
            GetSearchResultUseCase(searchRepository)


    }


}