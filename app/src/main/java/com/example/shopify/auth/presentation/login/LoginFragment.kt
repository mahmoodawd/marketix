package com.example.shopify.auth.presentation.login

import android.app.Activity.RESULT_OK
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import com.example.shopify.R
import com.example.shopify.auth.domain.entities.AuthState
import com.example.shopify.auth.presentation.googleauth.GoogleAuthClient
import com.example.shopify.auth.presentation.getValue
import com.example.shopify.databinding.FragmentLoginBinding
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class LoginFragment : Fragment() {
    lateinit var binding: FragmentLoginBinding

    lateinit var navController: NavController

    private val viewModel: LoginViewModel by viewModels()

    private val googleAuthClient by lazy {
        GoogleAuthClient(
            requireActivity().applicationContext,
            Identity.getSignInClient(requireActivity().applicationContext)
        )
    }

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

        binding.googleBtnView.googleBtn.setOnClickListener {
            /*lifecycleScope.launch {
                val signInIntentSender = googleAuthClient.signIn()
                val intent = IntentSenderRequest.Builder(
                    signInIntentSender ?: return@launch
                ).build()
                authResultLauncher.launch(
                    intent
                )
            }*/
            Timber.tag("TAG").i("onGoogleButtonClicked: ")
        }

        binding.navToRegisterBtn.setOnClickListener {
            navController.navigate(R.id.action_loginFragment_to_signUpFragment)
        }

        binding.forgetPasswordBtn.setOnClickListener {
            navController.setGraph(R.navigation.auth_nav_graph)
            navController.navigate(R.id.action_loginFragment_to_passwordRecoveryFragment)
        }


        listenToLoginStatus()

        listenToGoogleSignInStatus()

    }

    private val authResultLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                lifecycleScope.launch {
                    val signInResult = googleAuthClient.signInUsingIntent(
                        result.data ?: return@launch
                    )
                    viewModel.onSignInResult(signInResult)
                }
            }
        }

    private fun login() {
        val email = binding.emailField.emailEt.getValue()
        val password = binding.passwordField.passwordEt.getValue()
        viewModel.login(email, password)
    }

    private fun listenToLoginStatus() {
        lifecycleScope.launch {
            viewModel.loginStateFlow.collectLatest {
                binding.loginProgressBar.visibility =
                    if (it == AuthState.Loading) View.VISIBLE else View.GONE

                when (it) {
                    is AuthState.Success -> {
                       navController.navigate(getString(R.string.homeFragmentDeepLink).toUri())
                        Toast.makeText(
                            requireContext(),
                            it.result.displayName,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    is AuthState.UnVerified -> {
                        showDialog()
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

    private fun listenToGoogleSignInStatus() {
        lifecycleScope.launch {
            viewModel.googleSignInState.collectLatest {
                binding.loginProgressBar.visibility =
                    if (it == AuthState.Loading) View.VISIBLE else View.GONE

                when (it) {
                    is AuthState.Success -> {
                        navController.setGraph(R.navigation.home_graph)
                        Toast.makeText(
                            requireContext(),
                            it.result.userName,
                            Toast.LENGTH_SHORT
                        ).show()
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


    private fun showDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage("Email not verified, Check Your Inbox")
            .setPositiveButton("Open Gmail app") { _, _ ->
            }
            .show()
    }
}