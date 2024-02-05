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


@Composable
fun LoginScreen(navController: NavHostController, viewModel: LoginViewModel = hiltViewModel()) {
    val loginState by remember {
        viewModel.loginState

    }
    val authState by viewModel.loginRequestResult.collectAsState()
    LaunchedEffect(authState) {
        if (authState is Resource.Success && authState.data != null) {
            viewModel.saveProfile(authState.data!!)
            viewModel.updateLoginStatus(true)
            navController.navigate("home_screen")
            viewModel.resetStates()
            Log.d("loginDataResponse", "ProfileData: ${authState.data}")
        }
    }
    when (authState) {
        is Resource.Idle -> {
        }
        is Resource.Loading -> {
            LinearProgressIndicator()
        }
        is Resource.Success -> {
            if (authState.data != null) {
                Toast(message = authState.data!!.message)
            }
        }
        is Resource.Error -> {
            Toast(message = authState.message.toString())
            Log.d("loginDataResponse", "errorMessage: ${authState.message.toString()}")
        }
    }

    Surface(modifier = Modifier
        .fillMaxSize()) {
        Column(modifier = Modifier.padding(top = 12.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .navigationBarsPadding()
                    .imePadding()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

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

                        MediumTitleText(
                            modifier = Modifier
                                .padding(top = AppTheme.dimens.paddingLarge)
                                .fillMaxWidth(),
                            text = stringResource(id = R.string.app_name),
                            textAlign = TextAlign.Center
                        )
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
                        TitleText(
                            modifier = Modifier.padding(top = AppTheme.dimens.paddingLarge),
                            text = stringResource(id = R.string.login_heading_text)
                        )
                        LoginInputs(
                            loginState = loginState,
                            onEmailChange = { inputString ->
                                viewModel.onUiEvent(
                                    loginUiEvent = LoginUiEvent.EmailChanged(
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
                        )
                        NormalButton(
                            modifier = Modifier.padding(top = AppTheme.dimens.paddingLarge),
                            text = stringResource(id = R.string.login_button_text),
                            onClick = {
                                viewModel.onUiEvent(loginUiEvent = LoginUiEvent.Submit)
                                if ( loginState.isLoginSuccessful){
                                    viewModel.performLogin(viewModel.loginState.value.email.trim(),
                                        viewModel.loginState.value.password.trim())


                                }
                            }
                        )

                    }
                }
                Row(
                    modifier = Modifier.padding(AppTheme.dimens.paddingNormal),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = stringResource(id = R.string.do_not_have_account))
                    Text(
                        modifier = Modifier
                            .padding(start = AppTheme.dimens.paddingExtraSmall)
                            .clickable {
                                navController.navigate("register")
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

