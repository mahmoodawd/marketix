package com.example.shopify.auth.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopify.R
import com.example.shopify.auth.domain.usecases.LogInUseCase
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
class LoginViewModel @Inject constructor(
    @Dispatcher(Dispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    private val logInUseCase: LogInUseCase,
) :
    ViewModel() {

    private val _loginState: MutableStateFlow<LoginState.Display> =
        MutableStateFlow(LoginState.Display())
    val loginState = _loginState.asStateFlow()

    private val _snackBarFlow: MutableSharedFlow<Int> =
        MutableSharedFlow()
    val snackBarFlow = _snackBarFlow.asSharedFlow()


    val currentUser: FirebaseUser?
        get() = logInUseCase.currentUser


    init {
        if (currentUser != null && currentUser!!.isEmailVerified) {
            _loginState.update { it.copy(success = true) }
        }
    }

    fun login(email: String, password: String) =
        viewModelScope.launch(ioDispatcher) {
            if (email.isBlank() || password.isBlank()) {

                _snackBarFlow.emit(R.string.error_empty_fields)

            } else {
                _loginState.update { it.copy(loading = true) }

                logInUseCase<FirebaseUser>(email, password).collectLatest { response ->
                    when (response) {
                        is Response.Success -> {
                            _loginState.update { it.copy(success = true, loading = false) }
                        }

                        is Response.Failure -> {
                            when (response.error) {
                                "emailNotVerifiedException" -> _loginState.update {
                                    it.copy(
                                        unVerified = true,
                                        loading = false
                                    )
                                }

                                null -> Unit
                                else -> {
                                    _snackBarFlow.emit(R.string.failed_message)
                                    _loginState.update {
                                        it.copy(
                                            unVerified = false,
                                            loading = false
                                        )
                                    }
                                }
                            }
                        }

                        is Response.Loading -> {
                            _loginState.update { it.copy(loading = true) }

                        }


                    }

                }


            }
        }
}



