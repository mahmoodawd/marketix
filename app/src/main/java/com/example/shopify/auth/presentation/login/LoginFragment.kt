package com.example.shopify.auth.presentation.login

import android.app.Activity.RESULT_OK
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.shopify.R
import com.example.shopify.auth.domain.entities.AuthState
import com.example.shopify.auth.presentation.getValue
import com.example.shopify.databinding.FragmentLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {
    lateinit var binding: FragmentLoginBinding

    lateinit var navController: NavController

    private val viewModel: LoginViewModel by viewModels()


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
            signInWithGoogle()
        }

        binding.navToRegisterBtn.setOnClickListener {
            navController.navigate(R.id.action_loginFragment_to_signUpFragment)
        }

        binding.forgetPasswordBtn.setOnClickListener {
            navController.setGraph(R.navigation.auth_nav_graph)
            navController.navigate(R.id.action_loginFragment_to_passwordRecoveryFragment)
        }


        listenToLoginStatus()


    }


    private val authResultLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {

                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    setCurrentUser(account.idToken)
                } catch (e: ApiException) {
                    e.printStackTrace()
                }

            }
        }

    private fun setCurrentUser(idToken: String?) {
        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
        lifecycleScope.launch {

            FirebaseAuth.getInstance().signInWithCredential(firebaseCredential)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        navController.setGraph(R.navigation.home_graph)
                    }
                }

        }
    }


    private fun signInWithGoogle() {
        val googleSignInOptions = GoogleSignInOptions.Builder()
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        val client = GoogleSignIn.getClient(requireContext(), googleSignInOptions)
        authResultLauncher.launch(client.signInIntent)
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
                        navController.setGraph(R.navigation.home_graph)
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


    private fun showDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage("Email not verified, Check Your Inbox")
            .setPositiveButton("Open Gmail app") { _, _ ->
            }
            .show()
    }
}