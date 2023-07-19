package com.example.shopify.utils.navigation

import android.location.Geocoder
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.example.shopify.home.presentation.HomeFragment
import com.example.shopify.settings.presenation.address.map.AddressFragment
import com.example.shopify.utils.connectivity.ConnectivityObserver
import javax.inject.Inject

class FragmentFactory  @Inject constructor(
    private val connectivityObserver: ConnectivityObserver,
    private val englishGeoCoder: Geocoder,

    ): FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when(className){


            HomeFragment::class.java.name ->{
                HomeFragment(connectivityObserver)
            }
            AddressFragment::class.java.name ->{
                AddressFragment(englishGeoCoder)
            }
            else -> return super.instantiate(classLoader, className)
        }
    }


}