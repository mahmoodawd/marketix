package com.example.shopify.settings.di

import android.content.Context
import android.location.Geocoder
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.Granularity
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.FragmentScoped
import java.util.Locale

@Module
@InstallIn(FragmentComponent::class)
object SettingsFragmentModule{



    @Provides
    @FragmentScoped
    fun providesLocationRequestBuilder() = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY,0).apply {
        setMinUpdateDistanceMeters(500f)
        setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
        setWaitForAccurateLocation(true)
    }.build()

    @Provides
    @FragmentScoped
    fun providesLocationClient(@ActivityContext context: Context) = LocationServices.getFusedLocationProviderClient(context)


    @Provides
    @FragmentScoped
    fun providesLocationManager(@ApplicationContext context: Context) : LocationManager
    {
        return context.getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
    }


    @Provides
    @FragmentScoped
    fun providesEnglishGeoCoder(@ActivityContext context: Context) = Geocoder(context, Locale.US)




}