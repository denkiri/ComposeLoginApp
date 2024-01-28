package com.example.loginapp.screens.login
import android.content.Context
import android.content.res.Configuration.UI_MODE_NIGHT_YES
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.loginapp.R
import com.example.loginapp.components.MediumTitleText
import com.example.loginapp.components.NormalButton
import com.example.loginapp.components.TitleText
import com.example.loginapp.data.Resource
import com.example.loginapp.ui.theme.AppTheme
import com.example.loginapp.ui.theme.LoginAppTheme
import com.example.loginapp.screens.login.state.LoginUiEvent
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.loginapp.components.Toast

@Composable
fun LoginScreen(navController: NavHostController, viewModel: LoginViewModel = hiltViewModel()) {
    val loginState by remember {
        viewModel.loginState

    }
    fun Context.showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    var isLoading by remember { mutableStateOf(false) }
    // Display login information based on the result
    val loginStateB by viewModel.loginResult.collectAsState()
    when (loginStateB) {
        is Resource.Idle -> {

        }
        is Resource.Loading -> {

            // Show loading indicator
            LinearProgressIndicator()

        }
        is Resource.Success -> {
            // Show login success message or handle accordingly
            isLoading=false
            Log.d("loginuser", loginStateB.data!!.message)
            viewModel.saveProfile(loginStateB.data!!)
          Toast(message = loginStateB.data!!.message)

        }
        is Resource.Error -> {
            // Show error message or handle accordingly
           // Toast(message = loginStateB.)
        }
    }


    Surface(modifier = Modifier
        .padding(3.dp)
        .fillMaxSize()) {

        Column(modifier = Modifier.padding(top = 12.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally) {



//            if (loginInfo.data == null) {
//                Row() {
//                    LinearProgressIndicator()
//                    Text(text = "Loading...")
//                }
//
//            }else{
//            Greeting(loginInfo.data.profile.second_name)
//            }
            //  Log.d("Deets", "BookDetailsScreen: ${bookInfo.data.toString()}")
       // Login()
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
                                    isLoading = true
                                    viewModel.performLogin(viewModel.loginState.value.emailOrMobile.trim(),
                                        viewModel.loginState.value.password.trim()
                                    )

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

@Composable
fun Login() {

        }


fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview
@Composable
fun LoginScreenPreview() {
    LoginAppTheme {
        Login()
    }

}

@Composable
@Preview(uiMode = UI_MODE_NIGHT_YES)
fun LoginScreenDarkPreview() {
    LoginAppTheme {
        Login()
    }


}