package com.example.loginapp.screens.splash
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.util.Log
import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.loginapp.R
import com.example.loginapp.data.Resource
import com.example.loginapp.navigation.Screens
import com.example.loginapp.screens.login.LoginViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavHostController,viewModel: LoginViewModel = hiltViewModel()) {
    LaunchedEffect(Unit) {
        viewModel.fetchLoginStatus()
    }
    val loginStatusResult by viewModel.loginStatusResultB.collectAsState()
    val scale = remember {
        Animatable(0f)
    }
    LaunchedEffect(key1 = true) {
        scale.animateTo(
            targetValue = 360f,
            animationSpec = tween(
                durationMillis = 1000,
                delayMillis = 200
            )
        )



        scale.animateTo(targetValue = 0.9f,
            animationSpec = tween(durationMillis = 800,
                easing = {
                    OvershootInterpolator(8f)
                        .getInterpolation(it)
                }))
        delay(2000L)
        Log.d("loginStatusResponse", "LoginStatus: ${loginStatusResult.data.toString()}")
        if(loginStatusResult.data==true) {
            navController.navigate("first_screen")
            viewModel.resetStates()
        }
        else{
            navController.navigate("login")
           viewModel.resetStates()}





    }

    Splash(degrees = scale.value)

}

@Composable
fun Splash(degrees: Float) {
    if (isSystemInDarkTheme()) {
        Box(
            modifier = Modifier
                .background(Color.Black)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                modifier = Modifier.rotate(degrees = degrees).size(150.dp),
                painter = painterResource(id = R.drawable.ic_launcher_round),
                contentDescription = stringResource(R.string.app_logo)
            )
        }
    } else {
        Box(
            modifier = Modifier
                .background(Brush.verticalGradient(listOf(Color.White, Color.White)))
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                modifier = Modifier.rotate(degrees = degrees).size(150.dp),
                painter = painterResource(id = R.drawable.ic_launcher_round),
                contentDescription = stringResource(R.string.app_logo)
            )
        }
    }
}

@Composable
@Preview
fun SplashScreenPreview() {
    Splash(degrees = 0f)
}

@Composable
@Preview(uiMode = UI_MODE_NIGHT_YES)
fun SplashScreenDarkPreview() {
    Splash(degrees = 0f)
}