package com.example.shopify.checkout.di

import com.example.shopify.checkout.data.remote.CartAndCheckOutRemoteDataSource
import com.example.shopify.checkout.data.remote.CartAndCheckOutRemoteDataSourceImpl
import com.example.shopify.checkout.data.repository.CartAndCheckoutRepositoryImpl
import com.example.shopify.checkout.domain.repository.CartAndCheckoutRepository
import com.example.shopify.checkout.domain.usecase.DeleteCartItemUseCase
import com.example.shopify.checkout.domain.usecase.GetCartItemsUseCase
import com.example.shopify.checkout.domain.usecase.UpdateCartItemUseCase
import com.example.shopify.checkout.domain.usecase.discountcode.DeleteDiscountCodeFromDatabaseUseCase
import com.example.shopify.checkout.domain.usecase.discountcode.GetDiscountCodeByIdUseCase
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

        @Provides
        @ViewModelScoped
        fun providesDeleteCartItemUseCase(repository: CartAndCheckoutRepository) : DeleteCartItemUseCase
        {
            return DeleteCartItemUseCase(repository)
        }


        @Provides
        @ViewModelScoped
        fun providesUpdateCartItemUseCase(repository: CartAndCheckoutRepository) : UpdateCartItemUseCase
        {
            return UpdateCartItemUseCase(repository)
        }

        @Provides
        @ViewModelScoped
        fun providesDeleteDiscountCodeUseCase(repository: CartAndCheckoutRepository) : DeleteDiscountCodeFromDatabaseUseCase
        {
            return DeleteDiscountCodeFromDatabaseUseCase(repository)
        }

        @Provides
        @ViewModelScoped
        fun providesGetDiscountCodeByIdUseCase(repository: CartAndCheckoutRepository) : GetDiscountCodeByIdUseCase
        {
            return GetDiscountCodeByIdUseCase(repository)
        }

    }
}