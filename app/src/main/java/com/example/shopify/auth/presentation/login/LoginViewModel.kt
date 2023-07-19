package com.example.shopify.auth.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopify.auth.domain.entities.AuthState
import com.example.shopify.auth.domain.repository.AuthRepository
import com.example.shopify.auth.domain.usecases.LogInUseCase
import com.example.shopify.auth.domain.usecases.LogOutUseCase
import com.example.shopify.auth.presentation.googleauth.SignInResult
import com.example.shopify.auth.presentation.googleauth.UserData
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val logInUseCase: LogInUseCase,
    private val logOutUseCase: LogOutUseCase,
    private val authRepository: AuthRepository
) :
    ViewModel() {
    private val _loginStateFlow = MutableStateFlow<AuthState<FirebaseUser>?>(null)
    val loginStateFlow = _loginStateFlow.asStateFlow()

    private val _googleSignInState = MutableStateFlow<AuthState<UserData>?>(null)
    val googleSignInState = _googleSignInState.asStateFlow()

    val currentUser: FirebaseUser?
        get() = authRepository.currentUser

    init {
        if (currentUser != null && currentUser!!.isEmailVerified) {
            _loginStateFlow.value = AuthState.Success(currentUser!!)
        }
    }

    fun login(email: String, password: String) =
        viewModelScope.launch {
            _loginStateFlow.value = AuthState.Loading
            val result = logInUseCase(email, password)
            _loginStateFlow.value = result
        }


    fun onSignInResult(result: SignInResult) {
        _googleSignInState.value = AuthState.Loading

        _googleSignInState.value = AuthState.Success(result.data!!)

    }

    fun logout() {
        logOutUseCase()
        _loginStateFlow.value = null
    }

}