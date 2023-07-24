package com.example.shopify.checkout.presentation.checkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.shopify.databinding.FragmentCheckOutBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CheckOutFragment : Fragment() {



    private lateinit var binding: FragmentCheckOutBinding


    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCheckOutBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()
        binding.editEmailIconImageView.setOnClickListener {
            navController.navigate(CheckOutFragmentDirections.actionCheckOutFragmentToEmailDialogFragment())
        }

        binding.editPhoneIconImageView.setOnClickListener {
            navController.navigate(CheckOutFragmentDirections.actionCheckOutFragmentToPhoneDialogFragment())
        }

        binding.checkOutButton.setOnClickListener {
            navController.navigate(CheckOutFragmentDirections.actionCheckOutFragmentToDiscountFragment())
        }

    }


}