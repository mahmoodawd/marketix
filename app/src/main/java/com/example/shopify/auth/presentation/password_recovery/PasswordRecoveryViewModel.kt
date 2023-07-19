package com.example.shopify.auth.presentation.password_recovery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopify.auth.domain.entities.AuthState
import com.example.shopify.auth.domain.usecases.ResetPasswordUseCase
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PasswordRecoveryViewModel @Inject constructor(
    private val resetPasswordUseCase: ResetPasswordUseCase
) : ViewModel() {
    private val _resetPasswordStateFlow = MutableStateFlow<AuthState<FirebaseUser>?>(null)
    val resetPasswordStateFlow = _resetPasswordStateFlow.asStateFlow()


    fun resetPassword(email: String) {
        viewModelScope.launch {
            _resetPasswordStateFlow.value = AuthState.Loading
            val result = resetPasswordUseCase(email)
            _resetPasswordStateFlow.value = result
        }
    }
}