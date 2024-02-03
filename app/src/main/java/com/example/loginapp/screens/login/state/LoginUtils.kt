package com.example.loginapp.screens.login.state
import com.example.loginapp.R
import com.example.loginapp.components.ErrorState
val emailEmptyErrorState = ErrorState(
    hasError = true,
    errorMessageStringResource = R.string.login_error_msg_empty_email
)

val passwordEmptyErrorState = ErrorState(
    hasError = true,
    errorMessageStringResource = R.string.login_error_msg_empty_password
)
val loginErrorState = ErrorState(
    hasError = true,
    errorMessageStringResource = R.string.login_error_user_not_found
)