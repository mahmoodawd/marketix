package com.example.shopify.auth.di

import com.example.shopify.auth.data.repository.AuthRepoImpl
import com.example.shopify.auth.domain.repository.AuthRepository
import com.example.shopify.auth.domain.usecases.AuthenticationUseCase
import com.example.shopify.auth.domain.usecases.LogOutUseCase
import com.example.shopify.auth.domain.usecases.SignInUseCase
import com.example.shopify.auth.domain.usecases.SignUpUseCase
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
    abstract fun bindAuthRepository(authRepoImpl: AuthRepoImpl): AuthRepository

    companion object {

        @Provides
        @ViewModelScoped
        fun provideAuthenticationUseCase(authRepoImpl: AuthRepoImpl): AuthenticationUseCase {

            return AuthenticationUseCase(
                signInUseCase = SignInUseCase(authRepoImpl),
                signUpUseCase = SignUpUseCase(authRepoImpl),
                logOutUseCase = LogOutUseCase(authRepoImpl),
                currentUser = authRepoImpl.currentUser
            )
        }
    }

}