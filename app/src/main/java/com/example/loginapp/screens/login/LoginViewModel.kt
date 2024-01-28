package com.example.loginapp.screens.login
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loginapp.components.ErrorState
import com.example.loginapp.data.Resource
import com.example.loginapp.repository.LoginRepository
import com.example.loginapp.screens.login.state.LoginErrorState
import com.example.loginapp.screens.login.state.LoginState
import com.example.loginapp.models.ProfileData
import com.example.loginapp.screens.login.state.LoginUiEvent
import com.example.loginapp.screens.login.state.emailOrMobileEmptyErrorState
import com.example.loginapp.screens.login.state.passwordEmptyErrorState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: LoginRepository) : ViewModel(){
    var loginState = mutableStateOf(LoginState())

    suspend fun loginMember(mobileNumber: String,password:String): Resource<ProfileData> {
        return repository.loginMember(mobileNumber, password)
    }
    fun onUiEvent(loginUiEvent: LoginUiEvent) {
        when (loginUiEvent) {

            // Email/Mobile changed
            is LoginUiEvent.EmailOrMobileChanged -> {
                loginState.value = loginState.value.copy(
                    emailOrMobile = loginUiEvent.inputValue,
                    errorState = loginState.value.errorState.copy(
                        emailOrMobileErrorState = if (loginUiEvent.inputValue.trim().isNotEmpty())
                            ErrorState()
                        else
                            emailOrMobileEmptyErrorState
                    )
                )
            }

            // Password changed
            is LoginUiEvent.PasswordChanged -> {
                loginState.value = loginState.value.copy(
                    password = loginUiEvent.inputValue,
                    errorState = loginState.value.errorState.copy(
                        passwordErrorState = if (loginUiEvent.inputValue.trim().isNotEmpty())
                            ErrorState()
                        else
                            passwordEmptyErrorState
                    )
                )
            }

            // Submit Login
            is LoginUiEvent.Submit -> {
                val inputsValidated = validateInputs()
                if (inputsValidated) {
                    // TODO Trigger login in authentication flow
                    //Log.d("Login now","Successful")
                    loginState.value = loginState.value.copy(isLoginSuccessful = true)
                  //  loginState.value = loginState.value.copy(emailOrMobile = loginState.value.emailOrMobile.trim())
                   // loginState.value = loginState.value.copy(password = loginState.value.password.trim())


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
        val emailOrMobileString = loginState.value.emailOrMobile.trim()
        val passwordString = loginState.value.password
        return when {

            // Email/Mobile empty
            emailOrMobileString.isEmpty() -> {
                loginState.value = loginState.value.copy(
                    errorState = LoginErrorState(
                        emailOrMobileErrorState = emailOrMobileEmptyErrorState
                    )
                )
                false
            }

            //Password Empty
            passwordString.isEmpty() -> {
                loginState.value = loginState.value.copy(
                    errorState = LoginErrorState(
                        passwordErrorState = passwordEmptyErrorState
                    )
                )
                false
            }
            // No errors
            else -> {
                // Set default error state
                loginState.value = loginState.value.copy(emailOrMobile = emailOrMobileString)

                loginState.value = loginState.value.copy(errorState = LoginErrorState())
                true
            }
        }
    }
    private val _loginResult = MutableStateFlow<Resource<ProfileData>>(Resource.Idle())
    val loginResult: StateFlow<Resource<ProfileData>> = _loginResult

    fun performLogin(mobile: String, password: String) {
        viewModelScope.launch {
            _loginResult.value = Resource.Loading() // Notify UI that login operation is in progress
            try {
                val loginResponse = repository.loginMember(mobile, password)
                if (loginResponse is Resource.Success) {
                    _loginResult.value = Resource.Success(loginResponse.data!!)
                } else {
                    _loginResult.value = Resource.Error("Login failed")
                }
            } catch (e: Exception) {
                _loginResult.value = Resource.Error("Login failed: ${e.message}")
            }
        }
    }
   fun saveProfile(data:ProfileData){
        repository.saveProfile(data)
    }
}