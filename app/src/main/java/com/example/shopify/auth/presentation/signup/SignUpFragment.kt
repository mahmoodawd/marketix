package com.example.shopify.auth.presentation.signup

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.shopify.R
import com.example.shopify.auth.domain.entities.AuthState
import com.example.shopify.auth.presentation.getValue
import com.example.shopify.databinding.FragmentSignUpBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


@AndroidEntryPoint
class SignUpFragment : Fragment() {
    lateinit var binding: FragmentSignUpBinding

    lateinit var navController: NavController

    private val viewModel: SignUpViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        binding.signUpBtn.setOnClickListener { signUp() }
        binding.navToRegisterBtn.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
        }
        listenToSignUpStatus()
    }

    private fun signUp() {
        val userName = binding.userNameField.userNameEt.getValue()
        val email = binding.emailField.emailEt.getValue().trim()
        val password = binding.passwordField.passwordEt.getValue()
        viewModel.signUp(userName, email, password)
    }

    private fun listenToSignUpStatus() {
        lifecycleScope.launch {
            viewModel.signUpStateFlow.collectLatest {
                binding.signUpProgressBar.visibility =
                    if (it == AuthState.Loading) View.VISIBLE else View.GONE

                when (it) {
                    is AuthState.EmailSent -> {
                        showVerificationDialog()
                        binding.userNameField.userNameEt.editText?.text?.clear()
                        binding.emailField.emailEt.editText?.text?.clear()
                        binding.passwordField.passwordEt.editText?.text?.clear()
                    }

                    is AuthState.Failure -> {
                        Toast.makeText(
                            requireContext(),
                            it.exception.localizedMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    else -> Unit

                }
            }
        }
    }

    private fun showVerificationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage("Verification mail sent, Check Your Inbox")
            .setPositiveButton("Open Gmail app") { _, _ ->
                try {

                    Intent(
                        Intent.ACTION_VIEW, Uri.parse(
                            "content://com.google.android.gm"
                        )
                    ).apply {
                        startActivity(this)
                    }

                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(requireContext(), "Gmail Not Installed", Toast.LENGTH_SHORT)
                        .show()
                }
            }.setNeutralButton("Ok") { _, _ ->

                navController.navigate(R.id.action_signUpFragment_to_loginFragment)
            }
            .show()
    }
}