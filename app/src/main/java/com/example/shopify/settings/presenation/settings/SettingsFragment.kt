package com.example.shopify.settings.presenation.settings

import android.Manifest
import android.R
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.work.BackoffPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.shopify.databinding.FragmentSettingsBinding
import com.example.shopify.settings.domain.model.CurrencyModel
import com.example.shopify.utils.connectivity.ConnectivityObserver
import com.example.shopify.utils.snackBarObserver
import com.example.shopify.utils.workmanager.ApiExchangeWorker
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Duration
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@AndroidEntryPoint
class SettingsFragment(

) : Fragment() {

    private lateinit var binding: FragmentSettingsBinding

    private lateinit var navController: NavController

    private val viewModel: SettingsViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        binding.addressRightArrowImageView.setOnClickListener {
            navController.navigate(SettingsFragmentDirections.actionSettingsFragmentToAllAddressesFragment())
        }

        binding.accountRightArrowImageView.setOnClickListener {
            navController.navigate(SettingsFragmentDirections.actionSettingsFragmentToAccountFragment())
        }

        binding.notificationSwitch.setOnCheckedChangeListener { compoundButton, checked ->
            viewModel.onEvent(SettingsIntent.SaveNotificationPref(getString(com.example.shopify.R.string.notification),checked))
        }


        binding.locationSwitch.setOnCheckedChangeListener { compoundButton, checked ->
            viewModel.onEvent(SettingsIntent.SaveLocationPref(getString(com.example.shopify.R.string.locationservice),checked))
        }


        stateObserver()

        
        


        binding.currencySpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    val item = parent.getItemAtPosition(position)
                    viewModel.onEvent(intent = SettingsIntent
                        .SaveCurrencyPref(getString(com.example.shopify.R.string.currency)
                            ,item.toString()))

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
    }



    private fun stateObserver() {
        lifecycleScope.launch(Dispatchers.IO) {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.state.collectLatest { state ->

                    withContext(Dispatchers.Main) {
                        binding.notificationSwitch.isChecked = state.notification
                        binding.locationSwitch.isChecked = state.LocationService
                        spinnerSetup(state.currencies,state.selectedCurrency)
                    }

                }
            }
        }
    }
    private fun spinnerSetup(arraySpinner: List<CurrencyModel>?,selectedItem : String) {
        val adapter = ArrayAdapter(requireContext(),
            R.layout.simple_spinner_item, arraySpinner!!.map { it.currency })
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        binding.currencySpinner.adapter = adapter
        binding.currencySpinner.setSelection(arraySpinner.indexOfFirst { it.currency == selectedItem }.coerceAtLeast(0))
    }

}