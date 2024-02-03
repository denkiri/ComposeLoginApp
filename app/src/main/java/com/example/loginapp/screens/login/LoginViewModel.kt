package com.example.loginapp.screens.login
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loginapp.components.ErrorState
import com.example.loginapp.data.Resource
import com.example.loginapp.models.Profile
import com.example.loginapp.repository.LoginRepository
import com.example.loginapp.screens.login.state.LoginErrorState
import com.example.loginapp.screens.login.state.LoginState
import com.example.loginapp.models.ProfileData
import com.example.loginapp.screens.login.state.LoginUiEvent
import com.example.loginapp.screens.login.state.emailEmptyErrorState
import com.example.loginapp.screens.login.state.passwordEmptyErrorState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: LoginRepository) : ViewModel(){
    var loginState = mutableStateOf(LoginState())
    fun resetStates() {
        // Reset or clear loginState here
       loginState.value = LoginState() // Reset to initial value or null
        _loginRequestResult.value = Resource.Idle()
        _profileResult.value=Resource.Idle()
        _loginStatusResultB.value=Resource.Idle()
    }

    private val _isLoading = MutableStateFlow(true) // Initialize as true to start loading
    val isLoading: StateFlow<Boolean> = _isLoading

    fun onUiEvent(loginUiEvent: LoginUiEvent) {
        when (loginUiEvent) {

            // Email/Mobile changed
            is LoginUiEvent.EmailChanged -> {
                loginState.value = loginState.value.copy(
                    email = loginUiEvent.inputValue,
                    errorState = loginState.value.errorState.copy(
                        emailErrorState = if (loginUiEvent.inputValue.trim().isNotEmpty())
                            ErrorState()
                        else
                            emailEmptyErrorState
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
                    isLoading
                    // TODO Trigger login in authentication flow
                    loginState.value = loginState.value.copy(isLoginSuccessful = true)


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
        val emailOrMobileString = loginState.value.email.trim()
        val passwordString = loginState.value.password
        return when {

            // Email/Mobile empty
            emailOrMobileString.isEmpty() -> {
                loginState.value = loginState.value.copy(
                    errorState = LoginErrorState(
                        emailErrorState = emailEmptyErrorState
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
                loginState.value = loginState.value.copy(email = emailOrMobileString)

                loginState.value = loginState.value.copy(errorState = LoginErrorState())
                true
            }
        }
    }
    private val _loginRequestResult = MutableStateFlow<Resource<ProfileData>>(Resource.Idle())
    val loginRequestResult: StateFlow<Resource<ProfileData>> = _loginRequestResult
    fun performLogin(email: String, password: String) {
        viewModelScope.launch {
            _loginRequestResult.value = Resource.Loading() // Notify UI that login operation is in progress
            try {
                val loginResponse = repository.loginMember(email, password)
                if (loginResponse is Resource.Success) {
                    _isLoading.value = false
                    _loginRequestResult.value = Resource.Success(loginResponse.data!!)
                } else {
                    _loginRequestResult.value = Resource.Error(loginResponse.message)
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                _isLoading.value = false
                _loginRequestResult.value = Resource.Error("Login failed: ${e.message}")
            }
        }
    }
    private val _profileResult = MutableStateFlow<Resource<Profile>>(Resource.Idle())
    val profileResult: StateFlow<Resource<Profile>> = _profileResult
    fun getProfile() {

            viewModelScope.launch {
                try {
                    // Call the repository function to get LiveData<Resource<Profile>>
                    val profileLiveData = repository.getProfile()

                    // Observe the LiveData from the repository to handle Resource states
                    profileLiveData.observeForever { resource ->
                        when (resource) {
                            is Resource.Loading -> {
                                _profileResult.value =
                                    Resource.Loading() // Notify UI that profile data is loading
                            }

                            is Resource.Success -> {
                                // Update _profileResult with the success data
                                _profileResult.value = resource
                                _isLoading.value = false
                            }

                            is Resource.Error -> {
                                // Update _profileResult with the error message
                                _profileResult.value = resource
                                _isLoading.value = false
                            }

                            else -> {}
                        }
                    }
                } catch (e: Exception) {
                    // Handle any exceptions
                    _profileResult.value = Resource.Error("Error: ${e.message}")
                }
            }
        }


   fun saveProfile(data:ProfileData){
        repository.saveProfile(data)
    }
    private val _loginStatusResult = MutableStateFlow<Resource<Unit>>(Resource.Idle())
    fun updateLoginStatus(isLoggedIn: Boolean) {
        viewModelScope.launch {
            _loginStatusResult.value = Resource.Loading()
            repository.setLoginStatus(isLoggedIn).collect { result ->
                _loginStatusResult.value = result
            }
        }
    }
    private val _loginStatusResultB = MutableStateFlow<Resource<Boolean?>>(Resource.Idle())
    val loginStatusResultB: StateFlow<Resource<Boolean?>> = _loginStatusResultB

    fun fetchLoginStatus() {
        viewModelScope.launch {
            _loginStatusResultB.value = Resource.Loading()
            repository.getLoginStatus().collect { result ->
                _loginStatusResultB.value = result
                _isLoading.value = false // Stop loading when successful
                // Update loading state based on the result
                if (result is Resource.Success) {
                    _isLoading.value = false // Stop loading when successful
                }
            }
        }
    }

}