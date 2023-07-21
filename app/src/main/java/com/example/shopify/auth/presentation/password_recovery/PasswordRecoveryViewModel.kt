package com.example.shopify.auth.presentation.password_recovery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopify.R
import com.example.shopify.auth.domain.usecases.ResetPasswordUseCase
import com.example.shopify.utils.hiltanotations.Dispatcher
import com.example.shopify.utils.hiltanotations.Dispatchers
import com.example.shopify.utils.response.Response
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PasswordRecoveryViewModel @Inject constructor(
    @Dispatcher(Dispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    private val resetPasswordUseCase: ResetPasswordUseCase
) : ViewModel() {

    private val _resetPasswordState: MutableStateFlow<PasswordRecoveryState.Display> =
        MutableStateFlow(PasswordRecoveryState.Display())
    val resetPasswordState = _resetPasswordState.asStateFlow()

    private val _snackBarFlow: MutableSharedFlow<Int> =
        MutableSharedFlow()
    val snackBarFlow = _snackBarFlow.asSharedFlow()

    fun resetPassword(email: String) {
        viewModelScope.launch(ioDispatcher) {

            _resetPasswordState.update { it.copy(loading = true) }

            resetPasswordUseCase<FirebaseUser>(email).collectLatest { response ->

                when (response) {
                    is Response.Success -> {
                        _resetPasswordState.update { it.copy(success = true, loading = false) }
                    }

                    is Response.Failure -> {
                        _resetPasswordState.update { it.copy(loading = false) }
                        _snackBarFlow.emit(R.string.failed_message)
                    }

                    is Response.Loading -> {
                        _resetPasswordState.update { it.copy(loading = true) }

                    }
                }
            }


        }
    }
}