package com.example.shopify.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopify.auth.domain.entities.AuthState
import com.example.shopify.auth.domain.repository.AuthRepository
import com.example.shopify.auth.domain.usecases.AuthenticationUseCase
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authenticationUseCase: AuthenticationUseCase
    ) :
    ViewModel() {
    private val _loginStateFlow = MutableStateFlow<AuthState<FirebaseUser>?>(null)
    val loginStateFlow = _loginStateFlow.asStateFlow()

    private val _signUpStateFlow = MutableStateFlow<AuthState<FirebaseUser>?>(null)
    val signUpStateFlow = _signUpStateFlow.asStateFlow()


    val currentUser: FirebaseUser?
        get() = authenticationUseCase.currentUser

    init {
        if (currentUser != null) {
            _loginStateFlow.value = AuthState.Success(currentUser!!)
        }
    }

    fun login(email: String, password: String) =
        viewModelScope.launch {
            val result = authenticationUseCase.signInUseCase(email, password)
            _loginStateFlow.value = result
        }

    fun signUp(name: String, email: String, password: String) =
        viewModelScope.launch {
            _signUpStateFlow.value = AuthState.Loading
            val result = authenticationUseCase.signUpUseCase(name, email, password)
            _signUpStateFlow.value = result
        }


    fun logout() {
        authenticationUseCase.logOutUseCase()
        _loginStateFlow.value = null
        _signUpStateFlow.value = null
    }

}