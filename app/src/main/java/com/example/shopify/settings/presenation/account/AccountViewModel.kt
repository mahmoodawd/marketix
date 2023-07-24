package com.example.shopify.settings.presenation.account

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopify.R
import com.example.shopify.settings.domain.usecase.account.GetUserImageUseCase
import com.example.shopify.settings.domain.usecase.account.GetUserNameUseCase
import com.example.shopify.settings.domain.usecase.account.GetUserPhoneUseCase
import com.example.shopify.settings.domain.usecase.account.UpdateUserImageUseCase
import com.example.shopify.settings.domain.usecase.account.UpdateUserNameUseCase
import com.example.shopify.settings.domain.usecase.account.UpdateUserPhoneUseCase
import com.example.shopify.settings.domain.usecase.validation.ValidatePasswordUseCase
import com.example.shopify.settings.domain.usecase.validation.ValidatePhoneUseCase
import com.example.shopify.settings.domain.usecase.validation.ValidateUserNameUseCase
import com.example.shopify.utils.hiltanotations.Dispatcher
import com.example.shopify.utils.hiltanotations.Dispatchers
import com.example.shopify.utils.response.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AccountViewModel @Inject constructor(
    @Dispatcher(Dispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    private val getUserNameUseCase: GetUserNameUseCase,
    private val getUserImageUseCase: GetUserImageUseCase,
    private val getUserPhoneUseCase: GetUserPhoneUseCase,
    private val updateUserImageUseCase: UpdateUserImageUseCase,
    private val updateUserNameUseCase: UpdateUserNameUseCase,
    private val updateUserPhoneUseCase: UpdateUserPhoneUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
    private val validateUserNameUseCase: ValidateUserNameUseCase,
    private val validatePhoneUseCase: ValidatePhoneUseCase
) : ViewModel() {


    private val _state: MutableStateFlow<AccountState> =
        MutableStateFlow(AccountState())
    val state = _state.asStateFlow()


    private val _snackBarFlow: MutableSharedFlow<Int> =
        MutableSharedFlow()
    val snackBarFlow = _snackBarFlow.asSharedFlow()


    fun onEvent(intent: AccountIntent) {
        when (intent) {
            AccountIntent.ConfirmUpdate -> {
                confirmUpdate()
            }

            is AccountIntent.NewImageUri -> {
                _state.update { it.copy(imageFromServerUri = intent.uri) }
            }

            is AccountIntent.NewPassword -> {
                _state.update { it.copy(password = intent.password) }
            }

            is AccountIntent.NewPhoneNumber -> {
                _state.update { it.copy(phone = intent.number) }
            }

            is AccountIntent.NewUserName -> {
                _state.update { it.copy(userName = intent.userName) }
            }


        }

    }

    private fun confirmUpdate() {

        viewModelScope.launch {

            val oldImageUri = getUserImageUseCase.execute<Uri>().data
            if (_state.value.imageFromServerUri != null &&  _state.value.imageFromServerUri != oldImageUri) {
                updateProfileImage()
            }

            if (!validateUserNameUseCase.execute(_state.value.userName)) {
                viewModelScope.launch {
                    _snackBarFlow.emit(R.string.username_invalid)

                }
            } else {

                updateUserName()
            }

            if (!validatePhoneUseCase.execute(_state.value.phone)) {
                if (_state.value.phone.isNotEmpty()) {

                    viewModelScope.launch {
                        _snackBarFlow.emit(R.string.password_invalid)

                    }
                }
            } else {
                updateUserPhone()
            }
        }.invokeOnCompletion {
            viewModelScope.launch {
                _snackBarFlow.emit(R.string.account_updated_successfully)
            }
        }
    }


    private fun updateUserName() {
        viewModelScope.launch(ioDispatcher) {
            val userNameResult = updateUserNameUseCase.execute<Boolean>(_state.value.userName)

            if (userNameResult is Response.Failure) {
                _snackBarFlow.emit(R.string.failed_message)
            }
        }

    }

    private fun updateProfileImage() {
        viewModelScope.launch(ioDispatcher) {
            val imageResult = updateUserImageUseCase.execute<Boolean>(_state.value.imageFromServerUri!!)

            if (imageResult is Response.Failure) {
                _snackBarFlow.emit(R.string.failed_message)
            }
        }
    }

    private fun updateUserPhone() {
        viewModelScope.launch(ioDispatcher) {
            val phoneResult = updateUserPhoneUseCase.execute<Boolean>(_state.value.phone)

            if (phoneResult is Response.Failure) {
                _snackBarFlow.emit(R.string.failed_message)
            }else{
                _snackBarFlow.emit(R.string.successfull_message)
            }
        }
    }



    private fun getUserName() {
        viewModelScope.launch(ioDispatcher) {
            val response = getUserNameUseCase.execute<String>()
            if (response is Response.Success) {
                _state.update { it.copy(userName = response.data!!) }
            } else {
                _snackBarFlow.emit(R.string.failed_message)
            }
        }
    }

    private fun getUserImageUri() {
        viewModelScope.launch(ioDispatcher) {
            val response = getUserImageUseCase.execute<Uri>()
            if (response is Response.Success) {
                _state.update { it.copy(imageFromServerUri = response.data) }
            } else {
                _snackBarFlow.emit(R.string.failed_message)
            }
        }
    }

    private fun getUserPhone() {
        viewModelScope.launch(ioDispatcher) {
            val response = getUserPhoneUseCase.execute<String>()
            if (response is Response.Success) {
                _state.update { it.copy(phone = response.data!!) }
            } else {
                _snackBarFlow.emit(R.string.failed_message)
            }
        }
    }


    init {
        getUserName()
        getUserPhone()
        getUserImageUri()
    }

}