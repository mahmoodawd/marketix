package com.example.shopify.auth.presentation

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.shopify.R
import com.example.shopify.databinding.FragmentPasswordRecoveryBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class PasswordRecoveryFragment : Fragment() {
    private lateinit var binding: FragmentPasswordRecoveryBinding
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
            if (binding.emailEt.emailEt.getValue().isNotBlank()) {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Email Sent")
                    .setMessage("Reset Email Sent, Check Your Inbox!")
                    .setPositiveButton("Back To Login") { _, _ ->
                        findNavController()
                            .navigate(R.id.action_passwordRecoveryFragment_to_loginFragment)
                    }
                    .show()
            } else {
                Toast.makeText(requireContext(), "Email is Empty", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
