package com.example.shopify.settings.di

import com.example.shopify.settings.data.local.LocalDataSource
import com.example.shopify.settings.data.local.LocalDataSourceImpl
import com.example.shopify.settings.data.remote.RemoteDataSource
import com.example.shopify.settings.data.remote.RemoteDataSourceImpl
import com.example.shopify.settings.data.repository.SettingsRepositoryImpl
import com.example.shopify.settings.domain.repository.SettingsRepository
import com.example.shopify.settings.domain.usecase.GetAllCurrenciesUseCase
import com.example.shopify.settings.domain.usecase.account.GetUserImageUseCase
import com.example.shopify.settings.domain.usecase.account.GetUserNameUseCase
import com.example.shopify.settings.domain.usecase.account.GetUserPhoneUseCase
import com.example.shopify.settings.domain.usecase.account.UpdateUserImageUseCase
import com.example.shopify.settings.domain.usecase.account.UpdateUserNameUseCase
import com.example.shopify.settings.domain.usecase.account.UpdateUserPhoneUseCase
import com.example.shopify.settings.domain.usecase.dataStore.ReadBooleanDataStoreUseCase
import com.example.shopify.settings.domain.usecase.dataStore.ReadStringFromDataStoreUseCase
import com.example.shopify.settings.domain.usecase.dataStore.SaveBooleanToDataStoreUseCase
import com.example.shopify.settings.domain.usecase.dataStore.SaveStringToDataStoreUseCase
import com.example.shopify.settings.domain.usecase.validation.ValidatePasswordUseCase
import com.example.shopify.settings.domain.usecase.validation.ValidatePhoneUseCase
import com.example.shopify.settings.domain.usecase.validation.ValidateUserNameUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped


@Module
@InstallIn(ViewModelComponent::class)
abstract class SettingsViewModelModule {

    @Binds
    @ViewModelScoped
    abstract fun bindsLocalDataSource(localDataStoreImpl: LocalDataSourceImpl) : LocalDataSource


    @Binds
    @ViewModelScoped
    abstract fun bindsRemoteDataSource(remoteDataStoreImpl: RemoteDataSourceImpl) : RemoteDataSource
    @Binds
    @ViewModelScoped
   abstract fun bindsSettingsRepository(repositoryImpl: SettingsRepositoryImpl) : SettingsRepository


    companion object{
        @Provides
        @ViewModelScoped
        fun providesReadStringFromDataStoreUseCase(repository: SettingsRepository) : ReadStringFromDataStoreUseCase
        {
            return ReadStringFromDataStoreUseCase(repository)
        }


        @Provides
        @ViewModelScoped
        fun providesSaveStringToDataStoreUseCase(repository: SettingsRepository) : SaveStringToDataStoreUseCase
                = SaveStringToDataStoreUseCase(repository)


        @Provides
        @ViewModelScoped
        fun providesGetAllCurrenciesUseCase(repository: SettingsRepository) : GetAllCurrenciesUseCase
                = GetAllCurrenciesUseCase(repository)


        @Provides
        @ViewModelScoped
        fun providesSaveBooleanToDataStoreUseCase(repository: SettingsRepository) : SaveBooleanToDataStoreUseCase
                = SaveBooleanToDataStoreUseCase(repository)

        @Provides
        @ViewModelScoped
        fun providesReadBooleanFromDataStoreUseCase(repository: SettingsRepository) : ReadBooleanDataStoreUseCase
        {
            return ReadBooleanDataStoreUseCase(repository)
        }


        @Provides
        @ViewModelScoped
        fun providesGetUserImageUseCase(repository: SettingsRepository) : GetUserImageUseCase
        {
            return GetUserImageUseCase(repository)
        }


        @Provides
        @ViewModelScoped
        fun providesGetUserNameUseCase(repository: SettingsRepository) : GetUserNameUseCase
        {
            return GetUserNameUseCase(repository)
        }

        @Provides
        @ViewModelScoped
        fun providesGetUserPhoneUseCase(repository: SettingsRepository) : GetUserPhoneUseCase
        {
            return GetUserPhoneUseCase(repository)
        }

        @Provides
        @ViewModelScoped
        fun providesUpdateUserPhoneUseCase(repository: SettingsRepository) : UpdateUserPhoneUseCase
        {
            return UpdateUserPhoneUseCase(repository)
        }

        @Provides
        @ViewModelScoped
        fun providesUpdateUserImageUseCase(repository: SettingsRepository) : UpdateUserImageUseCase
        {
            return UpdateUserImageUseCase(repository)
        }

        @Provides
        @ViewModelScoped
        fun providesUpdateUserNameUseCase(repository: SettingsRepository) : UpdateUserNameUseCase
        {
            return UpdateUserNameUseCase(repository)
        }

        @Provides
        @ViewModelScoped
        fun providesValidatePasswordUseCase() : ValidatePasswordUseCase
        {
            return ValidatePasswordUseCase()
        }


        @Provides
        @ViewModelScoped
        fun providesValidatePhoneUseCase() : ValidatePhoneUseCase
        {
            return ValidatePhoneUseCase()
        }


        @Provides
        @ViewModelScoped
        fun providesValidateUserNameUseCase() : ValidateUserNameUseCase
        {
            return ValidateUserNameUseCase()
        }
    }
}