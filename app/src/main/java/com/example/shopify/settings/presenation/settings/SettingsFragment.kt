package com.example.shopify.settings.presenation.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.shopify.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding

    private lateinit var navController: NavController

    private val viewModel : SettingsViewModel by viewModels()

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
            navController.navigate(SettingsFragmentDirections.actionSettingsFragmentToAddressFragment())
        }

        binding.accountRightArrowImageView.setOnClickListener {
            navController.navigate(SettingsFragmentDirections.actionSettingsFragmentToAccountFragment())
        }

        viewModel.getAllCurrencies()

    }
}