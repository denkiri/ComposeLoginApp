# LoginRegisteAppWithJetpackCompose
A simple Android application containing login and registration built using Jetpack compose.
The app shows a clean way of handling login and registration state along with nested navigation.
# Overview
1. Login with simple empty validations
2. Registration with simple empty validations
3. State management for input field updates, button event update and navigation with the help of viewmodel
4. Nested navigation showing two different navigation routes for authenticated and unauthenticated route
5. Room local database to store [Profile](https://github.com/denkiri/ComposeLoginApp/blob/master/app/src/main/java/com/example/loginapp/models/Profile.kt) information
6. Used retrofit to make HTTP requests to [AutApi](https://github.com/denkiri/ComposeLoginApp/blob/master/app/src/main/java/com/example/loginapp/network/AuthApi.kt)
7. Dependency injection with [Hilt](https://github.com/denkiri/ComposeLoginApp/blob/master/app/src/main/java/com/example/loginapp/di/AppModule.kt)
8. MVVM Architecture
<img src="https://developer.android.com/static/topic/libraries/architecture/images/final-architecture.png" width="640" height="540">

