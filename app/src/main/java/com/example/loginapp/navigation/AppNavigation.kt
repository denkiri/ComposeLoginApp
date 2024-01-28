package com.example.loginapp.navigation
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.loginapp.screens.login.LoginScreen
import com.example.loginapp.screens.login.LoginViewModel
import com.example.loginapp.screens.splash.SplashScreen

@ExperimentalComposeUiApi
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController,
        startDestination = Screens.SplashScreen.name ){

         composable(Screens.SplashScreen.name) {
             SplashScreen(navController = navController)
         }
        composable(Screens.LoginScreen.name) {
            val loginViewModel = hiltViewModel<LoginViewModel>()
            LoginScreen(navController = navController,viewModel=loginViewModel)
        }






    }

}