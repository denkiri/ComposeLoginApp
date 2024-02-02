package com.example.loginapp.screens.login
import android.content.Context
import android.util.Log
import android.widget.Toast
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.loginapp.R
import com.example.loginapp.components.MediumTitleText
import com.example.loginapp.components.NormalButton
import com.example.loginapp.components.TitleText
import com.example.loginapp.components.Toast
import com.example.loginapp.data.Resource
import com.example.loginapp.ui.theme.AppTheme
import com.example.loginapp.screens.login.state.LoginUiEvent
import androidx.compose.runtime.mutableStateOf


@Composable
fun LoginScreen(navController: NavHostController, viewModel: LoginViewModel = hiltViewModel()) {
    val loginState by remember {
        viewModel.loginState

    }


    // Display login information based on the result
    val loginStateB by viewModel.loginRequestResult.collectAsState()
    // UI

        // Use LaunchedEffect to navigate to the next screen once when loginStateB is a success
        LaunchedEffect(loginStateB) {
            if (loginStateB is Resource.Success && loginStateB.data != null) {
                viewModel.saveProfile(loginStateB.data!!)
                viewModel.updateLoginStatus(true)
                navController.navigate("home_screen")
                viewModel.resetStates()
                Log.d("loginDataResponse", "ProfileData: ${loginStateB.data}")
            }
        }
// Handle navigation back to the previous screen
    // UI based on login state
    when (loginStateB) {
        is Resource.Idle -> {
            // Handle idle state
        }
        is Resource.Loading -> {
            // Show loading indicator
            LinearProgressIndicator()
        }
        is Resource.Success -> {
            if (loginStateB.data != null) {
               Toast(message = loginStateB.data!!.message)
                // Handle success case
                // You can perform any other UI-related actions here if needed
            }
        }
        is Resource.Error -> {
            // Handle error case
            Toast(message = loginStateB.message.toString())
            Log.d("loginDataResponse", "errorMessage: ${loginStateB.message.toString()}")
        }
    }
    Surface(modifier = Modifier
        .padding(3.dp)
        .fillMaxSize()) {

        Column(modifier = Modifier.padding(top = 12.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally) {

            // Full Screen Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .navigationBarsPadding()
                    .imePadding()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Main card Content for Login
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

                        // Heading Jetpack Compose
                        MediumTitleText(
                            modifier = Modifier
                                .padding(top = AppTheme.dimens.paddingLarge)
                                .fillMaxWidth(),
                            text = stringResource(id = R.string.app_name),
                            textAlign = TextAlign.Center
                        )

                        // Login Logo
                        AsyncImage(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(128.dp)
                                .padding(top = AppTheme.dimens.paddingSmall),
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(data = R.drawable.ic_launcher_round)
                                .crossfade(enable = true)
                                .scale(Scale.FILL)
                                .build(),
                            contentDescription = stringResource(id = R.string.login_heading_text)
                        )

                        // Heading Login
                        TitleText(
                            modifier = Modifier.padding(top = AppTheme.dimens.paddingLarge),
                            text = stringResource(id = R.string.login_heading_text)
                        )

                        // Login Inputs Composable
                        LoginInputs(
                            loginState = loginState,
                            onEmailOrMobileChange = { inputString ->
                                viewModel.onUiEvent(
                                    loginUiEvent = LoginUiEvent.EmailOrMobileChanged(
                                        inputString
                                    )
                                )
                            },
                            onPasswordChange = { inputString ->
                                viewModel.onUiEvent(
                                    loginUiEvent = LoginUiEvent.PasswordChanged(
                                        inputString
                                    )
                                )
                            },
                           // onForgotPasswordClick = onNavigateToForgotPassword
                        )
                        // Login Submit Button
                        NormalButton(
                            modifier = Modifier.padding(top = AppTheme.dimens.paddingLarge),
                            text = stringResource(id = R.string.login_button_text),
                            onClick = {
                                viewModel.onUiEvent(loginUiEvent = LoginUiEvent.Submit)
                                if ( loginState.isLoginSuccessful){
                                    viewModel.performLogin(viewModel.loginState.value.emailOrMobile.trim(),
                                        viewModel.loginState.value.password.trim())


                                }
                            }
                        )

                    }
                }

                // Register Section
                Row(
                    modifier = Modifier.padding(AppTheme.dimens.paddingNormal),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Don't have an account?
                    Text(text = stringResource(id = R.string.do_not_have_account))

                    //Register
                    Text(
                        modifier = Modifier
                            .padding(start = AppTheme.dimens.paddingExtraSmall)
                            .clickable {
                                // onNavigateToRegistration.invoke()
                            },
                        text = stringResource(id = R.string.register),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

        }


    }


}
fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

