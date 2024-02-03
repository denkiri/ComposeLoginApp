package com.example.loginapp.screens.registration

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loginapp.components.ErrorState
import com.example.loginapp.data.Resource
import com.example.loginapp.models.ProfileData
import com.example.loginapp.repository.LoginRepository
import com.example.loginapp.screens.login.state.LoginState
import com.example.loginapp.screens.registration.state.RegistrationErrorState
import com.example.loginapp.screens.registration.state.RegistrationState
import com.example.loginapp.screens.registration.state.RegistrationUiEvent
import com.example.loginapp.screens.registration.state.confirmPasswordEmptyErrorState
import com.example.loginapp.screens.registration.state.emailEmptyErrorState
import com.example.loginapp.screens.registration.state.mobileNumberEmptyErrorState
import com.example.loginapp.screens.registration.state.passwordEmptyErrorState
import com.example.loginapp.screens.registration.state.passwordMismatchErrorState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class RegistrationViewModel  @Inject constructor(private val repository: LoginRepository) : ViewModel(){

    var registrationState = mutableStateOf(RegistrationState())
    private val _isLoading = MutableStateFlow(true) // Initialize as true to start loading
    val isLoading: StateFlow<Boolean> = _isLoading
    fun resetStates() {
        // Reset or clear loginState here
        registrationState.value = RegistrationState() // Reset to initial value or null
        _registerRequestResult.value = Resource.Idle()
    }
    private val _registerRequestResult = MutableStateFlow<Resource<ProfileData>>(Resource.Idle())
    val registerRequestResult: StateFlow<Resource<ProfileData>> = _registerRequestResult
    fun performRegistration(email: String, password: String) {
        viewModelScope.launch {
            _registerRequestResult.value = Resource.Loading() // Notify UI that login operation is in progress
            try {
                val registerResponse = repository.registerMember(email, password)
                if (registerResponse is Resource.Success) {
                    _isLoading.value = false
                    _registerRequestResult.value = Resource.Success(registerResponse.data!!)
                } else {
                    _registerRequestResult.value = Resource.Error(registerResponse.message)
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                _isLoading.value = false
                _registerRequestResult.value = Resource.Error("Registration failed: ${e.message}")
            }
        }
    }



    /**
     * Function called on any login event [RegistrationUiEvent]
     */
    fun onUiEvent(registrationUiEvent: RegistrationUiEvent) {
        when (registrationUiEvent) {

            // Email id changed event
            is RegistrationUiEvent.EmailChanged -> {
                registrationState.value = registrationState.value.copy(
                    emailId = registrationUiEvent.inputValue,
                    errorState = registrationState.value.errorState.copy(
                        emailIdErrorState = if (registrationUiEvent.inputValue.trim().isEmpty()) {
                            // Email id empty state
                            emailEmptyErrorState
                        } else {
                            // Valid state
                            ErrorState()
                        }

                    )
                )
            }

            // Password changed event
            is RegistrationUiEvent.PasswordChanged -> {
                registrationState.value = registrationState.value.copy(
                    password = registrationUiEvent.inputValue,
                    errorState = registrationState.value.errorState.copy(
                        passwordErrorState = if (registrationUiEvent.inputValue.trim().isEmpty()) {
                            // Password Empty state
                            passwordEmptyErrorState
                        } else {
                            // Valid state
                            ErrorState()
                        }

                    )
                )
            }

            // Confirm Password changed event
            is RegistrationUiEvent.ConfirmPasswordChanged -> {
                registrationState.value = registrationState.value.copy(
                    confirmPassword = registrationUiEvent.inputValue,
                    errorState = registrationState.value.errorState.copy(
                        confirmPasswordErrorState = when {

                            // Empty state of confirm password
                            registrationUiEvent.inputValue.trim().isEmpty() -> {
                                confirmPasswordEmptyErrorState
                            }

                            // Password is different than the confirm password
                            registrationState.value.password.trim() != registrationUiEvent.inputValue -> {
                                passwordMismatchErrorState
                            }

                            // Valid state
                            else -> ErrorState()
                        }
                    )
                )
            }


            // Submit Registration event
            is RegistrationUiEvent.Submit -> {
                val inputsValidated = validateInputs()
                if (inputsValidated) {
                    // TODO Trigger registration in authentication flow
                    registrationState.value =
                        registrationState.value.copy(isRegistrationSuccessful = true)
                }
            }

        }
    }

    /**
     * Function to validate inputs
     * Ideally it should be on domain layer (usecase)
     * @return true -> inputs are valid
     * @return false -> inputs are invalid
     */
    private fun validateInputs(): Boolean {
        val emailString = registrationState.value.emailId.trim()
        val passwordString = registrationState.value.password.trim()
        val confirmPasswordString = registrationState.value.confirmPassword.trim()

        return when {

            // Email empty
            emailString.isEmpty() -> {
                registrationState.value = registrationState.value.copy(
                    errorState = RegistrationErrorState(
                        emailIdErrorState = emailEmptyErrorState
                    )
                )
                false
            }


            //Password Empty
            passwordString.isEmpty() -> {
                registrationState.value = registrationState.value.copy(
                    errorState = RegistrationErrorState(
                        passwordErrorState = passwordEmptyErrorState
                    )
                )
                false
            }

            //Confirm Password Empty
            confirmPasswordString.isEmpty() -> {
                registrationState.value = registrationState.value.copy(
                    errorState = RegistrationErrorState(
                        confirmPasswordErrorState = confirmPasswordEmptyErrorState
                    )
                )
                false
            }

            // Password and Confirm Password are different
            passwordString != confirmPasswordString -> {
                registrationState.value = registrationState.value.copy(
                    errorState = RegistrationErrorState(
                        confirmPasswordErrorState = passwordMismatchErrorState
                    )
                )
                false
            }

            // No errors
            else -> {
                // Set default error state
                registrationState.value =
                    registrationState.value.copy(errorState = RegistrationErrorState())
                true
            }
        }
    }
}