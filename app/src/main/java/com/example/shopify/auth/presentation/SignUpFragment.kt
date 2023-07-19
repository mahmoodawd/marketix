package com.example.shopify.auth.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.shopify.auth.domain.entities.AuthState
import com.example.shopify.databinding.FragmentSignUpBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SignUpFragment : Fragment() {
    lateinit var binding: FragmentSignUpBinding
    private val viewModel: AuthViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.signUpBtn.setOnClickListener { signUp() }

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
                binding.progressBar.visibility =
                    if (it == AuthState.Loading) View.VISIBLE else View.GONE

                when (it) {
                    is AuthState.Success -> {
                        Toast.makeText(requireContext(), it.result.displayName, Toast.LENGTH_SHORT)
                            .show()
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
}