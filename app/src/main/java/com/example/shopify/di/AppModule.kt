package com.example.shopify.di

import android.app.NotificationManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import androidx.work.WorkManager
import com.example.shopify.BuildConfig
import com.example.shopify.R
import com.example.shopify.data.datastore.DataStoreUserPreferences
import com.example.shopify.data.datastore.DataStoreUserPreferencesImpl
import com.example.shopify.data.remote.AuthorizationInterceptor
import com.example.shopify.data.remote.ExchangeApi
import com.example.shopify.data.remote.ShopifyRemoteInterface
import com.example.shopify.data.room.LocationDatabase
import com.example.shopify.settings.data.local.AddressDao
import com.example.shopify.settings.data.remote.RemoteCountriesInterface
import com.example.shopify.utils.connectivity.ConnectivityObserver
import com.example.shopify.utils.connectivity.NetworkConnectivityObserver
import com.google.firebase.auth.FirebaseAuth
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



    @Singleton
    @Provides
    fun providesNotificationManager(@ApplicationContext context: Context) : NotificationManager
    {
        return context.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
    }


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
    fun provideFirebaseStorage() : FirebaseStorage = FirebaseStorage.getInstance()


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
    fun providesCountriesRetrofitApi(): RemoteCountriesInterface
            = Retrofit.Builder()
        .baseUrl(BuildConfig.COUNTRIES_API)
        .addConverterFactory(
            GsonConverterFactory.create()).build()
        .create(RemoteCountriesInterface::class.java)



    @Singleton
    @Provides
    fun providesExchangeRetrofitApi(): ExchangeApi
            = Retrofit.Builder()
        .baseUrl(BuildConfig.EXCHANGE_API)
        .addConverterFactory(
            GsonConverterFactory.create()).build()
        .create(ExchangeApi::class.java)


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
    @Singleton
    @Provides
    fun providesWorkManager(@ApplicationContext context: Context) : WorkManager
    {
        return WorkManager.getInstance(context)
    }


}