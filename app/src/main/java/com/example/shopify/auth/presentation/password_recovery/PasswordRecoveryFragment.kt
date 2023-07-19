package com.example.shopify.auth.presentation.password_recovery

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.shopify.R
import com.example.shopify.auth.domain.entities.AuthState
import com.example.shopify.auth.presentation.getValue
import com.example.shopify.databinding.FragmentPasswordRecoveryBinding
import com.example.shopify.utils.response.Response
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PasswordRecoveryFragment : Fragment() {
    private lateinit var binding: FragmentPasswordRecoveryBinding

    private val viewModel: PasswordRecoveryViewModel by viewModels()

    private val navController: NavController by lazy {
        findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentPasswordRecoveryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.continueBtn.setOnClickListener {
            val email = binding.emailEt.emailEt.getValue()
            if (email.isNotBlank()) {
                viewModel.resetPassword(email)
            } else {
                Toast.makeText(requireContext(), "Email is Empty", Toast.LENGTH_SHORT).show()
            }
        }

        listenToEmailSendingStatus()
    }


    private fun listenToEmailSendingStatus() {
        lifecycleScope.launch {
            viewModel.resetPasswordStateFlow.collectLatest { state ->
                binding.passwordResetProgressBar.visibility =
                    if (state is AuthState.Loading)
                        View.VISIBLE else View.GONE

                when (state) {
                    is AuthState.EmailSent -> {
                        showDialog()
                    }

                    is AuthState.Failure -> {
                        Toast.makeText(
                            requireContext(),
                            state.exception.localizedMessage,
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
            .setTitle("Email Sent")
            .setMessage("Reset Email Sent, Check Your Inbox!")
            .setPositiveButton("Back To Login") { _, _ ->
                navController
                    .navigate(R.id.action_passwordRecoveryFragment_to_loginFragment)
            }
            .show()
    }
}
