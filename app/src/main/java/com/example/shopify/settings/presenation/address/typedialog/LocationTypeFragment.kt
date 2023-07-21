package com.example.shopify.settings.presenation.address.typedialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.shopify.R
import com.example.shopify.databinding.FragmentLocationTypeBinding


class LocationTypeFragment : DialogFragment() {


    private lateinit var binding: FragmentLocationTypeBinding
    private lateinit var controller: NavController

    override fun getTheme() = R.style.RoundedCornersDialog


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLocationTypeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        controller = NavHostFragment.findNavController(this)
        binding.gpsButton.setOnClickListener {
            controller.previousBackStackEntry!!.savedStateHandle[getString(R.string.type)] =
                getString(R.string.gps)
            dismiss()
        }


        binding.mapButton.setOnClickListener {
            controller.previousBackStackEntry!!.savedStateHandle[getString(R.string.type)] =
                getString(R.string.map)
            dismiss()
        }

    }

    override fun onResume() {
        super.onResume()
        setFullScreen()
    }

    private fun DialogFragment.setFullScreen() {
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}