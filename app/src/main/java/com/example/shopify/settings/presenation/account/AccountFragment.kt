package com.example.shopify.settings.presenation.account

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.shopify.databinding.FragmentAccountBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.userProfileChangeRequest
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AccountFragment(private val firebaseAuth: FirebaseAuth) : Fragment() {

    private lateinit var binding: FragmentAccountBinding

    private lateinit var navController: NavController


   private val imagePicker =
        registerForActivityResult(ActivityResultContracts.GetContent()) { result ->
            result?.let {
                getBitmapFromUri(result)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
         val user = firebaseAuth.currentUser
        binding.cameraImageview.setOnClickListener {
            imagePicker.launch("image/*")
        }

        binding.fullNameTextInputLayout.editText?.setText(user?.displayName)
        binding.phoneTextInputLayout.editText?.setText(user?.phoneNumber)

        binding.confirmButton.setOnClickListener {
            user?.apply {
                updateProfile(userProfileChangeRequest {
                    displayName =   binding.fullNameTextInputLayout.editText?.text.toString()
                })
            }

        }
    }


    private fun getBitmapFromUri(imageUri: Uri) {
         Glide
            .with(requireContext())
            .asBitmap()
            .load(imageUri)
             .into(binding.profileImage)
    }


}