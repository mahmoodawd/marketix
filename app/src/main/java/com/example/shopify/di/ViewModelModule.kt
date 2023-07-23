package com.example.shopify.di

import com.example.shopify.domain.usecase.dataStore.ReadBooleanDataStoreUseCase
import com.example.shopify.domain.usecase.dataStore.ReadStringFromDataStoreUseCase
import com.example.shopify.domain.usecase.dataStore.SaveBooleanToDataStoreUseCase
import com.example.shopify.domain.usecase.dataStore.SaveStringToDataStoreUseCase
import com.example.shopify.settings.domain.repository.SettingsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped


@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {

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
    fun providesSaveBooleanToDataStoreUseCase(repository: SettingsRepository) : SaveBooleanToDataStoreUseCase
            = SaveBooleanToDataStoreUseCase(repository)

    @Provides
    @ViewModelScoped
    fun providesReadBooleanFromDataStoreUseCase(repository: SettingsRepository) : ReadBooleanDataStoreUseCase
    {
        return ReadBooleanDataStoreUseCase(repository)
    }


}