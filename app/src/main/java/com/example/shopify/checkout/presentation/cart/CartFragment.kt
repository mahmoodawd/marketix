package com.example.shopify.checkout.presentation.cart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.shopify.R
import com.example.shopify.databinding.ActivityMainBinding
import com.example.shopify.databinding.FragmentCartBinding


class CartFragment : Fragment() {


    private lateinit var binding: FragmentCartBinding

    private lateinit var navController: NavController


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()

        binding.checkOutButton.setOnClickListener {
           navController.navigate(CartFragmentDirections.actionCartFragmentToCheckOutFragment())
        }

    }
}