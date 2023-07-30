package com.example.shopify.favorites.di

import com.example.shopify.favorites.domain.repository.FavoritesRepository
import com.example.shopify.favorites.domain.usecase.GetFavoriteProductsUseCase
import com.example.shopify.favorites.domain.usecase.RemoveDraftOrderUseCase
import com.example.shopify.favorites.data.local.FavoriteProductsLocalDataSource
import com.example.shopify.favorites.data.local.LocalDataSource
import com.example.shopify.favorites.data.remote.FavoriteProductsRemoteDataSource
import com.example.shopify.favorites.data.remote.RemoteDataSource
import com.example.shopify.favorites.data.repository.FavoritesRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class FavoritesViewModelModule {


    @Binds
    @ViewModelScoped
    abstract fun bindsFavLocalDataSource(draftOrdersLocalDataSource: FavoriteProductsLocalDataSource): LocalDataSource

    @Binds
    @ViewModelScoped
    abstract fun bindsFavRemoteDataSource(draftOrdersRemoteDataSource: FavoriteProductsRemoteDataSource): RemoteDataSource

    @Binds
    @ViewModelScoped
    abstract fun bindsFavoritesRepository(favoritesRepo: FavoritesRepositoryImpl): FavoritesRepository

    companion object {
        @Provides
        @ViewModelScoped
        fun provideGetFavoritesUseCase(favoritesRepo: FavoritesRepository) =
            GetFavoriteProductsUseCase(favoritesRepo)

        @Provides
        @ViewModelScoped
        fun provideRemoveDraftOrderUseCase(favoritesRepository: FavoritesRepository) =
            RemoveDraftOrderUseCase(favoritesRepository)

    }


}