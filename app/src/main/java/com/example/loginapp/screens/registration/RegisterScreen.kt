package com.example.loginapp.screens.registration
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.loginapp.R
import com.example.loginapp.components.NormalButton
import com.example.loginapp.components.SmallClickableWithIconAndText
import com.example.loginapp.components.TitleText
import com.example.loginapp.components.Toast
import com.example.loginapp.data.Resource
import com.example.loginapp.screens.registration.state.RegistrationUiEvent
import com.example.loginapp.ui.theme.AppTheme
@Composable
fun RegisterScreen(navController: NavHostController, viewModel: RegistrationViewModel = hiltViewModel()) {

    val registrationState by remember {
        viewModel.registrationState
    }
    val authState by viewModel.registerRequestResult.collectAsState()
    LaunchedEffect(authState) {
        if (authState is Resource.Success && authState.data != null) {
            viewModel.resetStates()
            navController.navigate("login")
            Log.d("loginDataResponse", "ProfileData: ${authState.data}")
        }
    }
    // UI based on login state
    when (authState) {
        is Resource.Idle -> {
            // Handle idle state
        }
        is Resource.Loading -> {
            // Show loading indicator
            LinearProgressIndicator()
        }
        is Resource.Success -> {
            if (authState.data != null) {
                Toast(message = authState.data!!.message)
                // Handle success case
                // You can perform any other UI-related actions here if needed
            }
        }
        is Resource.Error -> {
            // Handle error case
            Toast(message = authState.message.toString())
            Log.d("loginDataResponse", "errorMessage: ${authState.message.toString()}")
        }
    }
    Surface(modifier = Modifier
        .fillMaxSize()) {
        // Full Screen Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .imePadding()
                .verticalScroll(rememberScrollState())
        ) {

            // Back Button Icon
            SmallClickableWithIconAndText(
                modifier = Modifier
                    .padding(horizontal = AppTheme.dimens.paddingLarge)
                    .padding(top = AppTheme.dimens.paddingLarge),
                iconContentDescription = stringResource(id =R.string.navigate_back),
                iconVector = Icons.Outlined.ArrowBack,
                text = stringResource(id = R.string.back_to_login),
                onClick = { navController.navigate("login") }
            )


            // Main card Content for Registration
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(AppTheme.dimens.paddingLarge)
            ) {

                Column(
                    modifier = Modifier
                        .padding(horizontal = AppTheme.dimens.paddingLarge)
                        .padding(bottom = AppTheme.dimens.paddingExtraLarge)
                ) {

                    // Heading Registration
                    TitleText(
                        modifier = Modifier.padding(top = AppTheme.dimens.paddingLarge),
                        text = stringResource(id = R.string.registration_heading_text)
                    )

                    /**
                     * Registration Inputs Composable
                     */

                    /**
                     * Registration Inputs Composable
                     */
                    RegistrationInputs(
                        registrationState = registrationState,
                        onEmailIdChange = { inputString ->
                            viewModel.onUiEvent(
                                registrationUiEvent = RegistrationUiEvent.EmailChanged(
                                    inputValue = inputString
                                )
                            )
                        },
                        onPasswordChange = { inputString ->
                            viewModel.onUiEvent(
                                registrationUiEvent = RegistrationUiEvent.PasswordChanged(
                                    inputValue = inputString
                                )
                            )
                        },
                        onConfirmPasswordChange = { inputString ->
                            viewModel.onUiEvent(
                                registrationUiEvent = RegistrationUiEvent.ConfirmPasswordChanged(
                                    inputValue = inputString
                                )
                            )
                        }
                    )
                    // Registration Submit Button
                    NormalButton(
                        modifier = Modifier.padding(top = AppTheme.dimens.paddingExtraLarge),
                        text = stringResource(id = R.string.registration_button_text),
                        onClick = {
                            viewModel.onUiEvent(registrationUiEvent =RegistrationUiEvent.Submit)
                            if ( registrationState.isRegistrationSuccessful){
                                viewModel.performRegistration(viewModel.registrationState.value.emailId.trim(),
                                    viewModel.registrationState.value.password.trim())


                            }
                        }
                    )
                }
            }
            }
        }


}

