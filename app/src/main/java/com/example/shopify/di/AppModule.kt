package com.example.shopify.di

import android.content.Context
import androidx.room.Room
import com.example.shopify.BuildConfig
import com.example.shopify.R
import com.example.shopify.utils.connectivity.ConnectivityObserver
import com.example.shopify.utils.connectivity.NetworkConnectivityObserver
import com.example.shopify.data.datastore.DataStoreUserPreferences
import com.example.shopify.data.datastore.DataStoreUserPreferencesImpl
import com.google.firebase.auth.FirebaseAuth
import com.example.shopify.data.remote.AuthorizationInterceptor
import com.example.shopify.data.remote.ShopifyRemoteInterface
import com.example.shopify.data.room.LocationDatabase
import com.example.shopify.settings.data.local.AddressDao
import com.example.shopify.settings.data.remote.RemoteCountriesInterface
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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

    @Singleton
    @Provides
    fun provideFirebaseStorage() : FirebaseStorage  = FirebaseStorage.getInstance()


    @Singleton
    @Provides
    fun provideFirebaseFireStore() : FirebaseFirestore  = FirebaseFirestore.getInstance()


    @Singleton
    @Provides
    fun providesAuthorizationInterceptor()  = AuthorizationInterceptor()

    @Singleton
    @Provides
    fun providesOkHttpClient(authorizationInterceptor: AuthorizationInterceptor) : OkHttpClient

    {
        return OkHttpClient.Builder()
            .addInterceptor(authorizationInterceptor)
            .build()
    }



    @Singleton
    @Provides
    fun providesShopifyRetrofitApi(okHttpClient: OkHttpClient): ShopifyRemoteInterface
            = Retrofit.Builder()
        .baseUrl(BuildConfig.API_BASE)
        .client(okHttpClient)
        .addConverterFactory(
            GsonConverterFactory.create()).build()
        .create(ShopifyRemoteInterface::class.java)


    @Singleton
    @Provides
    fun providesCountriesRetrofitApi(okHttpClient: OkHttpClient): RemoteCountriesInterface
            = Retrofit.Builder()
        .baseUrl(BuildConfig.COUNTRIES_API)
        .client(okHttpClient)
        .addConverterFactory(
            GsonConverterFactory.create()).build()
        .create(RemoteCountriesInterface::class.java)



    @Singleton
    @Provides
    fun providesWeatherDatabase(@ApplicationContext applicationContext: Context): LocationDatabase {
        return Room.databaseBuilder(
            applicationContext,
            LocationDatabase::class.java, applicationContext.getString(R.string.location_database)
        ).build()
    }


    @Singleton
    @Provides
    fun providesAddressesDao(database: LocationDatabase) : AddressDao
    {
        return  database.addressDao
    }


}