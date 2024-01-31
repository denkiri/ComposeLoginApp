package com.example.loginapp.navigation

import java.lang.IllegalArgumentException

enum class Screens {
     SplashScreen,
    LoginScreen,
    HomeScreen;


    companion object {
         fun fromRoute(route: String?): Screens
          = when(route?.substringBefore("/")) {
              SplashScreen.name -> SplashScreen
             LoginScreen.name -> LoginScreen
             HomeScreen.name ->HomeScreen
             null -> SplashScreen
             else -> throw IllegalArgumentException("Route $route is not recognized")
          }
    }


}