package com.example.loginapp.navigation

import java.lang.IllegalArgumentException

enum class Screens {
     SplashScreen,
    LoginScreen;


    companion object {
         fun fromRoute(route: String?): Screens
          = when(route?.substringBefore("/")) {
              SplashScreen.name -> SplashScreen
             LoginScreen.name -> LoginScreen
             null -> SplashScreen
             else -> throw IllegalArgumentException("Route $route is not recognized")
          }
    }


}