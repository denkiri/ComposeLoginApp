package com.example.loginapp.navigation
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.loginapp.FirstScreen
import com.example.loginapp.SecondScreen
import com.example.loginapp.ThirdScreen
import com.example.loginapp.screens.login.LoginScreen
import com.example.loginapp.screens.login.LoginViewModel
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
        composable("first_screen") {
            val loginViewModel = hiltViewModel<LoginViewModel>()
            FirstScreen(navController = navController, viewModel = loginViewModel)
        }
        composable("second_screen") {
            SecondScreen(navController = navController)
        }
        composable("third_screen") {
            ThirdScreen(navController = navController)
        }
        composable("login") {
            val loginViewModel = hiltViewModel<LoginViewModel>()
            LoginScreen(navController = navController,viewModel=loginViewModel)
        }
    }

}