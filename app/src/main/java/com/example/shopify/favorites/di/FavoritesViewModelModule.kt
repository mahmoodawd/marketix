package com.example.shopify.favorites.di

import com.example.shopify.favorites.data.local.FavoriteProductsLocalDataSource
import com.example.shopify.favorites.data.local.LocalDataSource
import com.example.shopify.favorites.data.remote.FavoriteProductsRemoteDataSource
import com.example.shopify.favorites.data.remote.RemoteDataSource
import com.example.shopify.favorites.data.repository.DraftOrdersRepoImp
import com.example.shopify.domain.repository.DraftOrdersRepository
import com.example.shopify.domain.usecase.GetDraftOrdersUseCase
import dagger.Binds
import dagger.Module
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
    abstract fun bindsFavoritesRepository(favoritesRepoImp: DraftOrdersRepoImp): DraftOrdersRepository

    companion object {
        fun provideGetFavoritesUseCase(draftOrdersRepository: DraftOrdersRepository) =
            GetDraftOrdersUseCase(draftOrdersRepository)
    }

}