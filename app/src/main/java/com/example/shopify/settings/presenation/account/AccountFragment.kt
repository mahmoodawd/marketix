package com.example.shopify.settings.presenation.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.shopify.R
import com.example.shopify.databinding.FragmentAccountBinding
import com.example.shopify.utils.snackBarObserver
import com.example.shopify.utils.ui.changeFlow
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@AndroidEntryPoint
class AccountFragment() : Fragment() {

    private lateinit var binding: FragmentAccountBinding

    private lateinit var navController: NavController

    private val viewModel: AccountViewModel by viewModels()
    private val imagePicker =
        registerForActivityResult(ActivityResultContracts.GetContent()) { result ->
            result?.let { uri ->
                viewModel.onEvent(AccountIntent.NewImageUri(uri = uri))
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
        binding.cameraImageview.setOnClickListener {
            imagePicker.launch("image/*")
        }
        requireActivity().snackBarObserver(viewModel.snackBarFlow)
        stateObserver()
        changeUserNameListener()
        changePhoneListener()

        binding.confirmButton.setOnClickListener {
            viewModel.onEvent(AccountIntent.ConfirmUpdate)
        }

        binding.backImageView.setOnClickListener {
            navController.popBackStack()
        }
    }


    private fun stateObserver() {
        lifecycleScope.launch(Dispatchers.IO)
        {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.state.collectLatest { state ->
                    withContext(Dispatchers.Main) {

                        val uri = state.newImageUri ?: state.imageFromServerUri
                        Glide
                            .with(requireContext())
                            .asBitmap()
                            .load(uri)
                            .error(R.drawable.profile_image)
                            .into(binding.profileImage)

                        binding.fullNameTextInputLayout.editText!!.setText(state.userName)
                        binding.phoneTextInputLayout.editText!!.setText(state.phone)
                    }
                }
            }
        }
    }


    private fun changeUserNameListener() {
        lifecycleScope.launch {
            binding.fullNameTextInputLayout.changeFlow().collectLatest {
                viewModel.onEvent(AccountIntent.NewUserName(it))
            }
        }
    }


    private fun changePhoneListener() {
        lifecycleScope.launch {
            binding.phoneTextInputLayout.changeFlow().collectLatest {
                viewModel.onEvent(AccountIntent.NewPhoneNumber(it))
            }
        }
    }


}