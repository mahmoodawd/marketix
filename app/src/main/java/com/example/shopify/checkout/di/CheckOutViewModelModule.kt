package com.example.shopify.checkout.di

import com.example.shopify.checkout.data.remote.CartAndCheckOutRemoteDataSource
import com.example.shopify.checkout.data.remote.CartAndCheckOutRemoteDataSourceImpl
import com.example.shopify.checkout.data.repository.CartAndCheckoutRepositoryImpl
import com.example.shopify.checkout.domain.repository.CartAndCheckoutRepository
import com.example.shopify.checkout.domain.usecase.GetCartItemsUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped


@Module
@InstallIn(ViewModelComponent::class)
abstract class CheckOutViewModelModule {



    @Binds
    @ViewModelScoped
    abstract fun bindsRemoteDataSource(cartAndCheckOutRemoteDataSourceImpl: CartAndCheckOutRemoteDataSourceImpl) : CartAndCheckOutRemoteDataSource

    @Binds
    @ViewModelScoped
   abstract fun bindsSettingsRepository(cartAndCheckoutRepositoryImpl: CartAndCheckoutRepositoryImpl) : CartAndCheckoutRepository


    companion object{


        @Provides
        @ViewModelScoped
        fun providesGetCartItemsUseCase(repository: CartAndCheckoutRepository) : GetCartItemsUseCase
        {
            return GetCartItemsUseCase(repository)
        }



    }
}