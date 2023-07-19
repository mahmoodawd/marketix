package com.example.shopify.auth.presentation.signup

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
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
import com.example.shopify.databinding.FragmentSignUpBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
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

        binding.googleBtnView.googleBtn.setOnClickListener {
            signInWithGoogle()
        }

        listenToSignUpStatus()
    }

    private val authResultLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {

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