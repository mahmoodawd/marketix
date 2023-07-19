package com.example.shopify.auth.presentation.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopify.auth.domain.entities.AuthState
import com.example.shopify.auth.domain.repository.AuthRepository
import com.example.shopify.auth.domain.usecases.AuthenticationUseCase
import com.example.shopify.auth.domain.usecases.SignUpUseCase
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase
) : ViewModel() {

    private val _signUpStateFlow = MutableStateFlow<AuthState<FirebaseUser>?>(null)
    val signUpStateFlow = _signUpStateFlow.asStateFlow()


    fun signUp(name: String, email: String, password: String) =
        viewModelScope.launch {
            _signUpStateFlow.value = AuthState.Loading
            val result = signUpUseCase(name, email, password)
            _signUpStateFlow.value = result
        }
}