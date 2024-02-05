package com.example.loginapp.screens.home

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.loginapp.R
import com.example.loginapp.components.Toast
import com.example.loginapp.data.Resource
import com.example.loginapp.models.Profile
import com.example.loginapp.screens.login.LoginViewModel
import com.example.loginapp.ui.theme.LoginAppTheme

@Composable
fun HomeScreen(navController: NavController, viewModel: LoginViewModel = hiltViewModel()) {

    LaunchedEffect(Unit) {
        viewModel.getProfile()
    }

    val profileState by viewModel.profileResult.collectAsState()

    when (profileState) {
        is Resource.Loading -> {
            // Show loading indicator
            LinearProgressIndicator()

        }
        is Resource.Success -> {
            // Show login success message or handle accordingly
            Log.d("profileDataResponse", "ProfileData: ${profileState.data.toString()}")

           // navController.navigate("first_screen")
        }
        is Resource.Error -> {
        }

        else -> {}
    }
    LoginAppTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
        ) {
            profileState.data?.let { BizCard(it) }
        }
    }
}
@Composable
private fun CreateImageProfile(modifier: Modifier=Modifier) {
    Surface(
        modifier
            .size(150.dp)
            .padding(5.dp),
        shape = CircleShape,
        border = BorderStroke(0.5.dp, Color.LightGray),
        shadowElevation = 4.dp
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_round),
            contentDescription = "profile image",
            modifier = modifier.size(135.dp),
            contentScale = ContentScale.Crop
        )
    }
}
@Composable
private fun BizCard(profileData: Profile) {
    Surface(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()) {
        ElevatedCard(modifier = Modifier
            .width(200.dp)
            .height(390.dp)
            .padding(12.dp),
            shape = RoundedCornerShape(corner = CornerSize(15.dp)),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            )) {
            Column(modifier = Modifier
                .height(300.dp)
                .fillMaxWidth(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally ) {
                CreateImageProfile()
                Divider()
                ProfileInfo(profileData)



            }
        }


    }

}

@Composable
private fun ProfileInfo(profileData: Profile) {
    Column(modifier = Modifier.padding(5.dp)) {
        Text(
            text = profileData.email,
            color = MaterialTheme.colorScheme.primary,
            fontStyle = FontStyle.Normal,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Text(
            text = "Android Compose Programmer",
            color = MaterialTheme.colorScheme.primary,
            fontStyle = FontStyle.Normal,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Text(
            text = "@thedeleCompose",
            color = MaterialTheme.colorScheme.primary,
            fontStyle = FontStyle.Normal,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}
