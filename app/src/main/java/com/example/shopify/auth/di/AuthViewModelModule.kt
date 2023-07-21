package com.example.shopify.auth.di

import com.example.shopify.auth.data.remote.CustomerRemoteDataSource
import com.example.shopify.auth.data.remote.RemoteDataSource
import com.example.shopify.auth.data.repository.AuthRepoImpl
import com.example.shopify.auth.domain.repository.AuthRepository
import com.example.shopify.auth.domain.usecases.CreateCustomerAccountUseCase
import com.example.shopify.auth.domain.usecases.LogInUseCase
import com.example.shopify.auth.domain.usecases.LogOutUseCase
import com.example.shopify.auth.domain.usecases.SignUpUseCase
import com.example.shopify.auth.domain.usecases.ValidateEmailUseCase
import com.example.shopify.auth.domain.usecases.ValidatePasswordUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class AuthViewModelModule {


    @Binds
    @ViewModelScoped
    abstract fun bindsCustomerRemoteDataSource(customerRemoteDataSource: CustomerRemoteDataSource): RemoteDataSource

    @Binds
    @ViewModelScoped
    abstract fun bindAuthRepository(authRepoImpl: AuthRepoImpl): AuthRepository

    companion object {
        @Provides
        @ViewModelScoped
        fun provideValidateEmailUseCase() = ValidateEmailUseCase()

        @Provides
        @ViewModelScoped
        fun provideValidatePasswordUseCase() = ValidatePasswordUseCase()

        @Provides
        @ViewModelScoped
        fun provideLogInUseCase(authRepoImpl: AuthRepoImpl): LogInUseCase =
            LogInUseCase(authRepoImpl)

        @Provides
        @ViewModelScoped
        fun provideSignUpUseCase(authRepoImpl: AuthRepoImpl): SignUpUseCase =
            SignUpUseCase(authRepoImpl)

        @Provides
        @ViewModelScoped
        fun provideLogOutUseCase(authRepoImpl: AuthRepoImpl): LogOutUseCase =
            LogOutUseCase(authRepoImpl)

        @Provides
        @ViewModelScoped
        fun provideCreateCustomerAccountUseCase(authRepoImpl: AuthRepoImpl): CreateCustomerAccountUseCase =
            CreateCustomerAccountUseCase(authRepoImpl)

    }
}