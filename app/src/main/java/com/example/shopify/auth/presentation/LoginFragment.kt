package com.example.shopify.auth.presentation

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
import com.example.shopify.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {
    lateinit var binding: FragmentLoginBinding

    lateinit var navController: NavController
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
         navController = findNavController()
        binding.signInBtn.setOnClickListener { login() }
        binding.navToRegisterTv.setOnClickListener {
            navController.navigate(R.id.action_loginFragment_to_signUpFragment)
        }
        binding.forgetPasswordBtn.setOnClickListener {
            navController.navigate(R.id.action_loginFragment_to_passwordRecoveryFragment)
        }

        listenToLoginStatus()
    }

    private fun login() {
        val email = binding.emailField.emailEt.getValue()
        val password = binding.passwordField.passwordEt.getValue()
        viewModel.login(email, password)
    }

    private fun listenToLoginStatus() {
        lifecycleScope.launch {
            viewModel.loginStateFlow.collectLatest {
                binding.progressBar.visibility =
                    if (it == AuthState.Loading) View.VISIBLE else View.GONE

                when (it) {
                    is AuthState.Success -> {
                       navController.setGraph(R.navigation.home_graph)
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