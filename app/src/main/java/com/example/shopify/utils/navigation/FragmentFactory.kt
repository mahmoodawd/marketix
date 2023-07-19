package com.example.shopify.utils.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.example.shopify.home.presentation.HomeFragment
import com.example.shopify.utils.connectivity.ConnectivityObserver
import javax.inject.Inject

class FragmentFactory  @Inject constructor(
    private val connectivityObserver: ConnectivityObserver

): FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when(className){
            HomeFragment::class.java.name ->{
                HomeFragment(connectivityObserver)
            }
            else -> return super.instantiate(classLoader, className)
        }
    }


}