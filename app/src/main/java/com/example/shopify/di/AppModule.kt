package com.example.shopify.di

import android.content.Context
import com.example.shopify.utils.connectivity.ConnectivityObserver
import com.example.shopify.utils.connectivity.NetworkConnectivityObserver
import com.example.shopify.data.datastore.DataStoreUserPreferences
import com.example.shopify.data.datastore.DataStoreUserPreferencesImpl
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providesConnectivityObserver(@ApplicationContext context: Context): ConnectivityObserver =
        NetworkConnectivityObserver(context = context)


    @Provides
    @Singleton
    fun provideUserDataStorePreferences(
        @ApplicationContext applicationContext: Context
    ): DataStoreUserPreferences {
        return DataStoreUserPreferencesImpl(applicationContext)
    }

    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

}