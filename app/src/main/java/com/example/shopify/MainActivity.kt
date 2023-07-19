package com.example.shopify

import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.shopify.databinding.ActivityMainBinding
import com.example.shopify.utils.connectivity.ConnectivityObserver
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    @Inject
    lateinit var connectivityObserver: ConnectivityObserver

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navController = Navigation.findNavController(this, R.id.navHostFragment)
        NavigationUI.setupWithNavController(binding.bottomNavigation, navController)
        currentFragmentObserver()
        bottomNavigationSelector()
        backPressedHandler()

    }


    private fun backPressedHandler() {
        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                  if (navController.currentDestination!!.id != R.id.homeFragment)
                  {
                      navController.setGraph(R.navigation.home_graph)
                  }else{
                        moveTaskToBack(true)
                  }
                }
            })
    }


    private fun bottomNavigationSelector() {


        binding.bottomNavigation.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {

                R.id.home_graph -> {
                    navController.setGraph(R.navigation.home_graph)
                }

                R.id.favorites_graph -> {
                    navController.setGraph(R.navigation.fav_nav_graph)
                }

                R.id.settings_graph -> {
                    navController.setGraph(R.navigation.settings_graph)
                }

                R.id.cartFragment -> {
                    navController.setGraph(R.navigation.settings_graph)
                    navController.navigate(getString(R.string.cartFragmentDeepLink).toUri())
                }

            }
            return@setOnItemSelectedListener true
        }
    }


    private fun currentFragmentObserver() {
        navController.addOnDestinationChangedListener { _: NavController?, _: NavDestination?, _: Bundle? ->
            when (navController.currentDestination!!.id) {
                R.id.homeFragment, R.id.favoritesFragment
                    , R.id.settingsFragment, R.id.cartFragment -> {
                    binding.bottomNavigation.visibility = View.VISIBLE
                }

                else -> {
                    binding.bottomNavigation.visibility = View.GONE
                }

            }

        }

    }


}
