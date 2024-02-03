package com.example.loginapp.screens.registration.state
/**
 * Registration Screen Events
 */
sealed class RegistrationUiEvent {
    data class EmailChanged(val inputValue: String) : RegistrationUiEvent()
    data class PasswordChanged(val inputValue: String) : RegistrationUiEvent()
    data class ConfirmPasswordChanged(val inputValue: String) : RegistrationUiEvent()
    object Submit : RegistrationUiEvent()
}