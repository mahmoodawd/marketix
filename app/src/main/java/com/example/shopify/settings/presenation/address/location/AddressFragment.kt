package com.example.shopify.settings.presenation.address.location

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.shopify.R
import com.example.shopify.databinding.FragmentAddressBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class AddressFragment : Fragment() {

    private lateinit var binding: FragmentAddressBinding

    private val viewModel: AddressViewModel by viewModels()

    private val supportMapFragment: SupportMapFragment by lazy {
        childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddressBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        googleMapHandler()
        stateObserver()

        binding.go.setOnClickListener {
            viewModel.onEvent(AddressIntent.SaveLastLocation)
        }
    }


    private fun stateObserver() {
        lifecycleScope.launch(Dispatchers.IO) {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.state.collectLatest { state ->

                    withContext(Dispatchers.Main){
                    binding.progressBar.visibility = if (state.loading) View.VISIBLE else View.GONE
                    }


                }
            }

        }
    }


    private fun googleMapHandler() {

        supportMapFragment.getMapAsync { googleMap ->


            googleMap.setOnMapLongClickListener { latLong ->

                newMapLocationIsSelected(latLong, googleMap)

            }

            googleMap.setOnMapLoadedCallback {
                viewModel.onEvent(
                    AddressIntent.MapLoaded
                )

                with(viewModel.state.value) {
                    latitude?.let {
                        setMarkerAndAnimateCamera(
                            LatLng(latitude.toDouble(), longitude!!.toDouble()),
                            googleMap
                        )
                    }
                }
            }

        }


    }

    private fun newMapLocationIsSelected(latLong: LatLng, googleMap: GoogleMap) {
        setMarkerAndAnimateCamera(latLong, googleMap)
        viewModel.onEvent(
            AddressIntent.NewLatLong(
                latLong.latitude,
                latLong.longitude
            )
        )
    }


    private fun setMarkerAndAnimateCamera(latLong: LatLng, googleMap: GoogleMap) {
        googleMap.apply {
            clear()
            addMarker(
                MarkerOptions()
                    .position(latLong)
            )

            val cameraPosition = CameraPosition.Builder()
                .target(latLong)
                .zoom(5f)
                .build()
            googleMap.animateCamera(
                CameraUpdateFactory.newCameraPosition(cameraPosition),
                1500,
                null
            )
        }

    }
}