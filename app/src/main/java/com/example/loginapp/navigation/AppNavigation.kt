package com.example.loginapp.navigation
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.loginapp.screens.home.HomeScreen
import com.example.loginapp.screens.login.LoginScreen
import com.example.loginapp.screens.login.LoginViewModel
import com.example.loginapp.screens.registration.RegisterScreen
import com.example.loginapp.screens.registration.RegistrationViewModel
import com.example.loginapp.screens.splash.SplashScreen
@ExperimentalComposeUiApi
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController,
        startDestination = "splash" ){

         composable("splash") {
             val loginViewModel = hiltViewModel<LoginViewModel>()
             SplashScreen(navController = navController, viewModel = loginViewModel)
         }
        composable("home_screen") {
            val loginViewModel = hiltViewModel<LoginViewModel>()
            HomeScreen(navController = navController, viewModel = loginViewModel)
        }
        composable("login") {
            val loginViewModel = hiltViewModel<LoginViewModel>()
            LoginScreen(navController = navController,viewModel=loginViewModel)
        }
        composable("register") {
            val registrationViewModel = hiltViewModel<RegistrationViewModel>()
            RegisterScreen(navController = navController,viewModel=registrationViewModel)
        }
    }

}