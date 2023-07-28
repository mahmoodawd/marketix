package com.example.shopify.utils.navigation

import android.location.Geocoder
import android.location.LocationManager
import android.provider.Settings
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.example.shopify.auth.presentation.login.LoginFragment
import com.example.shopify.auth.presentation.signup.SignUpFragment
import com.example.shopify.home.presentation.HomeFragment
import com.example.shopify.orders.presentation.orders.OrdersFragment
import com.example.shopify.settings.presenation.address.adresses.AllAddressesFragment
import com.example.shopify.settings.presenation.address.map.MapFragment
import com.example.shopify.settings.presenation.settings.SettingsFragment
import com.example.shopify.splash.SplashFragment
import com.example.shopify.utils.connectivity.ConnectivityObserver
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import javax.inject.Inject

class FragmentFactory  @Inject constructor(
    private val connectivityObserver: ConnectivityObserver,
    private val englishGeoCoder: Geocoder,
    private val locationClient : FusedLocationProviderClient,
    private val locationManager: LocationManager,
    private val locationRequest: LocationRequest,
    private val firebaseAuth: FirebaseAuth,
    private val firebaseStorage: FirebaseStorage,
    private val firebaseFirestore: FirebaseFirestore

    ): FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when(className){


            HomeFragment::class.java.name ->{
                HomeFragment(connectivityObserver)
            }

            OrdersFragment::class.java.name ->{
                OrdersFragment(connectivityObserver, firebaseAuth)
            }

            MapFragment::class.java.name ->{
                MapFragment(englishGeoCoder)
            }


            LoginFragment::class.java.name -> {
                LoginFragment(firebaseAuth)
            }

            SignUpFragment::class.java.name -> {
                SignUpFragment(firebaseAuth)
            }

            AllAddressesFragment::class.java.name ->{
                AllAddressesFragment(locationClient, locationRequest, locationManager, connectivityObserver)
            }


            SettingsFragment::class.java.name ->{
                SettingsFragment(firebaseAuth)
            }

            SplashFragment::class.java.name->{
                SplashFragment(firebaseAuth)
            }
            else -> return super.instantiate(classLoader, className)
        }
    }


}