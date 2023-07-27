package com.example.shopify.settings.presenation.address.write

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.shopify.R
import com.example.shopify.databinding.FragmentWriteAddressBinding
import com.example.shopify.utils.snackBarObserver
import com.example.shopify.utils.ui.changeFlow
import com.example.shopify.utils.ui.visibleIf
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@AndroidEntryPoint
class WriteAddressFragment : Fragment() {

    private lateinit var binding: FragmentWriteAddressBinding

    private lateinit var navController: NavController

    private val viewModel: WriteAddressViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWriteAddressBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        setLatLongArguments()
        stateObserver()
        changeAddressListener()
        navigateWhenAddressInserted()
        requireActivity().snackBarObserver(viewModel.snackBarFlow)
        binding.citiesSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    val item = parent.getItemAtPosition(position)
                    viewModel.onEvent(intent = WriteAddressIntent.NewSelectedCity(item.toString()))
                    binding.citiesSpinner.setSelection(position)

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        binding.saveAddress.setOnClickListener {
            viewModel.onEvent(WriteAddressIntent.SaveAddress(getString(R.string.addressType)))
        }


    }

    private fun navigateWhenAddressInserted()
    {
        lifecycleScope.launch {
            viewModel.addressInserted.shareIn(lifecycleScope, SharingStarted.Eagerly,0).collectLatest { resourceId ->
                navController.popBackStack(R.id.allAddressesFragment,false)
            }
        }
    }



    private fun setLatLongArguments()
    {
        val latitude = arguments?.getString(getString(R.string.latitudeType))
        val longitude = arguments?.getString(getString(R.string.longitude))
        viewModel.onEvent(WriteAddressIntent.NewLatLong(latitude!!.toDouble(),longitude!!.toDouble()))

    }


    private fun changeAddressListener() {
        lifecycleScope.launch {
            binding.addressTextInputLayout.changeFlow().collectLatest {
                viewModel.onEvent(WriteAddressIntent.NewAddress(it))
            }
        }
    }

    private fun stateObserver() {
        lifecycleScope.launch(Dispatchers.IO) {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.state.collectLatest { state ->
                    withContext(Dispatchers.Main) {
                        spinnerSetup(state.cities, state.selectedCity)
                        binding.addressTextInputLayout.editText!!.setText(state.address)
                        binding.progressBar visibleIf state.loading
                    }
                }
            }
        }
    }


    private fun spinnerSetup(arraySpinner: List<String>, selectedItem: String) {
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item, arraySpinner
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.citiesSpinner.adapter = adapter
        binding.citiesSpinner.setSelection(arraySpinner.indexOfFirst { it == selectedItem })
    }




}