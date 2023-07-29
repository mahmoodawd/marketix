package com.example.shopify.settings.presenation.settings


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.shopify.R
import com.example.shopify.databinding.FragmentSettingsBinding
import com.example.shopify.settings.domain.model.CurrencyModel
import com.example.shopify.utils.connectivity.ConnectivityObserver
import com.example.shopify.utils.ui.goneIf
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@AndroidEntryPoint
class SettingsFragment(
    private val firebaseAuth: FirebaseAuth,
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

            if (!viewModel.state.value.userIsGuest){
                navController.navigate(SettingsFragmentDirections.actionSettingsFragmentToAllAddressesFragment())
            }else{
                Snackbar.make(
                    requireActivity().window.decorView.rootView, getString(R.string.guest_message),
                    Snackbar.LENGTH_SHORT
                ).show()

            }

        }

        binding.accountRightArrowImageView.setOnClickListener {
            if (!viewModel.state.value.userIsGuest){

            navController.navigate(SettingsFragmentDirections.actionSettingsFragmentToAccountFragment())
            }else{

                Snackbar.make(
                    requireActivity().window.decorView.rootView, getString(R.string.guest_message),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        binding.notificationSwitch.setOnCheckedChangeListener { _, checked ->
            viewModel.onEvent(SettingsIntent.SaveNotificationPref(getString(com.example.shopify.R.string.notification),checked))
        }


        binding.locationSwitch.setOnCheckedChangeListener { _, checked ->
            viewModel.onEvent(SettingsIntent.SaveLocationPref(getString(com.example.shopify.R.string.locationservice),checked))
        }


        binding.backImageView.setOnClickListener {
            navController.popBackStack()
        }

        binding.logoutTextView.setOnClickListener {
            firebaseAuth.signOut()
           navController.navigate(SettingsFragmentDirections.actionSettingsFragmentToAuthenticationGraph())
        }


        stateObserver()

        
        


        binding.currencySpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
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
                        binding.logoutTextView goneIf   state.userIsGuest
                        spinnerSetup(state.currencies,state.selectedCurrency)
                    }

                }
            }
        }
    }



    private fun spinnerSetup(arraySpinner: List<CurrencyModel>?,selectedItem : String) {
        val adapter = ArrayAdapter(requireContext(),
            android.R.layout.simple_spinner_item, arraySpinner!!.map { it.currency })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.currencySpinner.adapter = adapter
        binding.currencySpinner.setSelection(arraySpinner.indexOfFirst { it.currency == selectedItem }.coerceAtLeast(0))
    }

}