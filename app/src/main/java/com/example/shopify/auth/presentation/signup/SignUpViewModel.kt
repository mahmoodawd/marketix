package com.example.shopify.auth.presentation.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopify.R
import com.example.shopify.auth.domain.usecases.SignUpUseCase
import com.example.shopify.auth.domain.usecases.ValidateEmailUseCase
import com.example.shopify.auth.domain.usecases.ValidatePasswordUseCase
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
class SignUpViewModel @Inject constructor(
    @Dispatcher(Dispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    private val signUpUseCase: SignUpUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase
) :
    ViewModel() {

    private val _signUpState: MutableStateFlow<SignUpState.Display> =
        MutableStateFlow(SignUpState.Display())
    val signUpState = _signUpState.asStateFlow()

    private val _snackBarFlow: MutableSharedFlow<Int> =
        MutableSharedFlow()
    val snackBarFlow = _snackBarFlow.asSharedFlow()


    fun signUp(userName: String, email: String, password: String) =
        viewModelScope.launch(ioDispatcher) {
            if (userName.isBlank() || email.isBlank() || password.isBlank()) {

                _snackBarFlow.emit(R.string.error_empty_fields)

            } else {

                if (!validateEmailUseCase(email)) {
                    _snackBarFlow.emit(R.string.error_invalid_email)
                } else if (!validatePasswordUseCase(password)) {
                    _snackBarFlow.emit(R.string.error_invalid_password)

                } else {

                    _signUpState.update { it.copy(loading = true) }

                    signUpUseCase<FirebaseUser>(
                        userName,
                        email,
                        password
                    ).collectLatest { response ->
                        when (response) {
                            is Response.Success -> {
                                _signUpState.update { it.copy(success = true, loading = false) }
                            }

                            is Response.Failure -> {
                                _signUpState.update { it.copy(loading = false) }
                                _snackBarFlow.emit(R.string.failed_message)
                            }

                            is Response.Loading -> {
                                _signUpState.update { it.copy(loading = true) }

                            }


                        }

                    }
                }
            }


        }

}



