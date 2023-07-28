package com.example.shopify.settings.presenation.address.adresses

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopify.R
import com.example.shopify.data.datastore.DataStoreUserPreferences
import com.example.shopify.databinding.FragmentAllAddressesBinding
import com.example.shopify.utils.connectivity.ConnectivityObserver
import com.example.shopify.utils.recycler.moveRecyclerItemListener
import com.example.shopify.utils.recycler.swipeRecyclerItemListener
import com.example.shopify.utils.snackBarObserver
import com.example.shopify.utils.ui.visibleIf
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Collections


@AndroidEntryPoint
class AllAddressesFragment(
    private val locationClient: FusedLocationProviderClient,
    private val locationRequest: LocationRequest,
    private val locationManager: LocationManager,
    private val connectivityObserver: ConnectivityObserver,
) : Fragment() {

    private lateinit var binding: FragmentAllAddressesBinding

    private lateinit var navController: NavController


    private val viewModel: AllAddressesViewModel by viewModels()


    private lateinit var locationCallback: LocationCallback

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions(),
    ) { permissions ->
        permissions.forEach { permission ->
            if (!permission.value) {
                return@registerForActivityResult
            }
        }
        getLastLocationFromGPS()
    }


    private val startLocationActivityForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        {
            if (!isLocationEnabled()) {
                return@registerForActivityResult
            }
            getLastLocationFromGPS()
        }


    private val openPermissionPageForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        {
            if (!checkPermission()) {
                return@registerForActivityResult
            }
            getLastLocationFromGPS()
        }

    private val addressesRecyclerAdapter by lazy {
        AddressesRecyclerAdapter { address ->
            navController.navigate(
                AllAddressesFragmentDirections.actionAllAddressesFragmentToWriteAddressFragment(
                    address.latitude.toString(),
                    address.longitude.toString()
                )
            )
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAllAddressesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        gpsLocationCallback()
        setUpAddressesRecyclerView()
        stateObserver()
        requireActivity().snackBarObserver(viewModel.snackBarFlow)
        locationTypeObserver()

        binding.fabPlusButton.setOnClickListener {
            navController.navigate(
                AllAddressesFragmentDirections.actionAllAddressesFragmentToLocationTypeDialog()
            )
        }


        binding.backImageView.setOnClickListener {
            navController.popBackStack()
        }
    }

    private fun setUpAddressesRecyclerView() {
        val linearLayoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.addressesRecyclerView.apply {

            swipeRecyclerItemListener { viewHolder ->
                if (viewModel.state.value.addresses[viewHolder.adapterPosition].isDefault) {
                    binding.addressesRecyclerView.adapter!!.notifyItemChanged(viewHolder.adapterPosition)
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.you_can_t_delete_the_default_address),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@swipeRecyclerItemListener
                }
                viewModel.onEvent(AllAddressesIntent.DeleteAddress(viewHolder.adapterPosition))
                viewModel.onEvent(AllAddressesIntent.GetAllAddresses)
            }

            moveRecyclerItemListener { draggedIndex, targetIndex ->
                viewModel.onEvent(AllAddressesIntent.ItemIsDragged(draggedIndex, targetIndex))
                binding.addressesRecyclerView.adapter!!.notifyItemMoved(draggedIndex, targetIndex)

            }
            layoutManager = linearLayoutManager
            adapter = addressesRecyclerAdapter
        }
    }

    private fun stateObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.state.collectLatest { state ->
                    if (state.addresses.isNotEmpty()) {
                        addressesRecyclerAdapter.submitList(state.addresses)
                    }
                    binding.progressBar visibleIf state.loading

                }
            }
        }
    }

    private fun locationTypeObserver() {
        val backStackEntry: NavBackStackEntry = navController.currentBackStackEntry!!
        lifecycleScope.launch {

            val locationTypeFlow = backStackEntry.savedStateHandle.getStateFlow(
                getString(com.example.shopify.R.string.type),
                ""
            )


            locationTypeFlow.collectLatest { locationType ->
                delay(200)
                if (locationType == getString(com.example.shopify.R.string.map)) {
                    navController.navigate(
                        AllAddressesFragmentDirections.actionAllAddressesFragmentToAddressFragment(
                            viewModel.state.value.latitude.toString(),
                            viewModel.state.value.longitude.toString()
                        )
                    )
                } else if (locationType == getString(com.example.shopify.R.string.gps)) {

                    if (!isLocationEnabled()) {
                        startLocationPage()
                        return@collectLatest
                    }


                    if (!viewModel.state.value.LocationService) {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.please_enable_location_services_from_settings_page),
                            Toast.LENGTH_SHORT
                        ).show()
                        return@collectLatest
                    }
                    if (checkPermission()) {
                        navController.navigate(
                            AllAddressesFragmentDirections.actionAllAddressesFragmentToWriteAddressFragment(
                                viewModel.state.value.latitude.toString(),
                                viewModel.state.value.longitude.toString()
                            )
                        )
                    } else {

                        Toast.makeText(
                            requireContext(),
                            getString(com.example.shopify.R.string.please_we_need_location_permission),
                            Toast.LENGTH_SHORT
                        ).show()
                        openPermissionsPage()
                    }
                } else {
                    requestPermission()
                }
                backStackEntry.savedStateHandle[getString(com.example.shopify.R.string.type)] = ""

            }
        }
    }

    private fun openPermissionsPage() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", requireContext().packageName, null)
        intent.data = uri
        openPermissionPageForResult.launch(intent)
    }


    private fun requestPermission() {
        requestPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            )
        )
    }

    private fun checkPermission(): Boolean {
        if (
            (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED) ||
            (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED)
        ) {
            return true
        }
        return false
    }

    private fun getLastLocationFromGPS() {
        if (!checkPermission()) {

            requestPermission(); return
        }
        if (!isLocationEnabled()) {

            startLocationPage();return
        }
        locationClient.requestLocationUpdates(
            locationRequest, locationCallback, Looper.myLooper()
        )
    }

    private fun startLocationPage() {
        Toast.makeText(
            requireContext(),
            getString(com.example.shopify.R.string.please_enable_your_location),
            Toast.LENGTH_SHORT
        ).show()
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startLocationActivityForResult.launch(intent)
    }


    private fun gpsLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                if (viewModel.state.value.LocationService) {
                    val location = locationResult.lastLocation
                    viewModel.onEvent(
                        AllAddressesIntent.LatLongFromGPS(
                            location!!.latitude,
                            location.longitude
                        )
                    )
                }
            }
        }
    }


    private fun isLocationEnabled(): Boolean {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }


    override fun onResume() {
        super.onResume()
        if (viewModel.state.value.customerId.isNotEmpty()) {
            viewModel.onEvent(AllAddressesIntent.GetAllAddresses)
        }
    }

}