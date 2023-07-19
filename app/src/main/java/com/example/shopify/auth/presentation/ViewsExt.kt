package com.example.shopify.auth.presentation

import com.example.shopify.databinding.UserNameEditTextBinding
import com.google.android.material.textfield.TextInputLayout

fun  TextInputLayout.getValue(): String = editText?.text.toString()