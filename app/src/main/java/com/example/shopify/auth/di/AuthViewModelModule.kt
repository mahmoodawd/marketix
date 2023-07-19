package com.example.shopify.auth.di

import com.example.shopify.auth.data.repository.AuthRepoImpl
import com.example.shopify.auth.domain.repository.AuthRepository
import com.example.shopify.auth.domain.usecases.AuthenticationUseCase
import com.example.shopify.auth.domain.usecases.LogOutUseCase
import com.example.shopify.auth.domain.usecases.LogInUseCase
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

    }

    /* companion object {

         @Provides
         @ViewModelScoped
         fun provideAuthenticationUseCase(authRepoImpl: AuthRepoImpl): AuthenticationUseCase {

             return AuthenticationUseCase(
                 signInUseCase = LogInUseCase(authRepoImpl),
                 signUpUseCase = SignUpUseCase(authRepoImpl),
                 logOutUseCase = LogOutUseCase(authRepoImpl),
                 currentUser = authRepoImpl.currentUser
             )
         }
     }
 */
}