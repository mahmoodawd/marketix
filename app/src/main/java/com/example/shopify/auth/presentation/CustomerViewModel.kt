package com.example.shopify.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopify.auth.data.dto.CustomerResponse
import com.example.shopify.auth.domain.usecases.CreateCustomerAccountUseCase
import com.example.shopify.utils.response.Response
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CustomerViewModel @Inject constructor(
    private val createCustomerAccountUseCase: CreateCustomerAccountUseCase
) : ViewModel() {

    fun createCustomerAccount(user: FirebaseUser) = viewModelScope.launch {

        createCustomerAccountUseCase<CustomerResponse>(user).collect { response ->
            when (response) {
                is Response.Success -> {

                    Timber.tag(this.javaClass.name).i(
                        "createCustomerAccount for ${response.data?.customer?.id}: OK"
                    )
                }

                is Response.Failure -> {

                    Timber.tag(this.javaClass.name)
                        .i("createCustomerAccount: Failed: ${response.error}")
                }

                is Response.Loading -> {

                }


            }
        }
    }
}