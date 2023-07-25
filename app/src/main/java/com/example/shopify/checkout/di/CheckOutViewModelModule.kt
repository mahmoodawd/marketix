package com.example.shopify.checkout.di

import com.example.shopify.checkout.data.local.CartAndCheckOutLocalDataSource
import com.example.shopify.checkout.data.local.CartAndCheckOutLocalDataSourceImpl
import com.example.shopify.checkout.data.remote.CartAndCheckOutRemoteDataSource
import com.example.shopify.checkout.data.remote.CartAndCheckOutRemoteDataSourceImpl
import com.example.shopify.checkout.data.repository.CartAndCheckoutRepositoryImpl
import com.example.shopify.checkout.domain.repository.CartAndCheckoutRepository
import com.example.shopify.checkout.domain.usecase.account.GetEmailUseCase
import com.example.shopify.checkout.domain.usecase.account.GetUserPhoneUseCase
import com.example.shopify.checkout.domain.usecase.address.GetAllAddressUseCase
import com.example.shopify.checkout.domain.usecase.cart.DeleteCartItemUseCase
import com.example.shopify.checkout.domain.usecase.cart.GetCartItemsUseCase
import com.example.shopify.checkout.domain.usecase.cart.UpdateCartItemUseCase
import com.example.shopify.checkout.domain.usecase.discountcode.DeleteDiscountCodeFromDatabaseUseCase
import com.example.shopify.checkout.domain.usecase.discountcode.GetAllDiscountCodeUseCase
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
    abstract fun bindsLocalDataSource(cartAndCheckOutLocalDataSourceImpl: CartAndCheckOutLocalDataSourceImpl) : CartAndCheckOutLocalDataSource


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


        @Provides
        @ViewModelScoped
        fun providesGetAllDiscountCodeUseCase(repository: CartAndCheckoutRepository) : GetAllDiscountCodeUseCase
        {
            return GetAllDiscountCodeUseCase(repository)
        }


        @Provides
        @ViewModelScoped
        fun providesGetAllAddressUseCase(repository: CartAndCheckoutRepository) : GetAllAddressUseCase
        {
            return GetAllAddressUseCase(repository)
        }


        @Provides
        @ViewModelScoped
        fun providesGetEmailUseCase(repository: CartAndCheckoutRepository) : GetEmailUseCase
        {
            return GetEmailUseCase(repository)
        }

        @Provides
        @ViewModelScoped
        fun providesGetUserPhoneUseCase(repository: CartAndCheckoutRepository) : GetUserPhoneUseCase
        {
            return GetUserPhoneUseCase(repository)
        }

    }
}