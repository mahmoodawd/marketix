package com.example.shopify.checkout.presentation.emaildialog

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.shopify.R
import com.example.shopify.databinding.FragmentCartBinding
import com.example.shopify.databinding.FragmentEmailDialogBinding


class EmailDialogFragment : DialogFragment() {


    private lateinit var binding: FragmentEmailDialogBinding
    private lateinit var controller: NavController

    override fun getTheme() = R.style.RoundedCornersDialog



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEmailDialogBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        controller = NavHostFragment.findNavController(this)

        binding.confirmButton.setOnClickListener {
            val email = binding.emailInputLayout.editText!!.text
            Log.d("emailCollector",email.toString())
            controller.previousBackStackEntry!!.savedStateHandle[getString(R.string.emailType)] = email.toString()
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