package com.example.shopify.settings.di

import android.content.Context
import android.location.Geocoder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.FragmentScoped
import java.util.Locale

@Module
@InstallIn(FragmentComponent::class)
object SettingsFragmentModule{




    @Provides
    @FragmentScoped
    fun providesEnglishGeoCoder(@ActivityContext context: Context) = Geocoder(context, Locale.US)




}