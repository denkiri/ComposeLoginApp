package com.example.loginapp.utils
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.sql.DriverManager
object NetworkUtils {
    private fun getNetworkInfo(context: Context): NetworkInfo? {
        return try {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            cm.activeNetworkInfo
        } catch (e: Exception) {
            Log.e("CheckConnectivity", "Exception: " + e.message)
            null
        }
    }

    fun hasInternetConnection(context: Context, onConnectionResult: (Boolean) -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            val isConnected = isConnected(context)
            if (isConnected) {
                try {
                    val urlc = URL("http://clients3.google.com/generate_204").openConnection() as HttpURLConnection
                    urlc.setRequestProperty("User-Agent", "Android")
                    urlc.setRequestProperty("Connection", "close")
                    urlc.connectTimeout = 1500
                    urlc.connect()
                    val isConnectedToInternet = urlc.responseCode == 204
                    onConnectionResult(isConnectedToInternet)
                } catch (e: IOException) {
                    Log.e("NetworkUtils", "Error checking internet connection", e)
                    onConnectionResult(false)
                }
            } else {
                Log.d("NetworkUtils", "No network available!")
                onConnectionResult(false)
            }
        }
    }

    private fun isConnected(context: Context): Boolean {
        val info = getNetworkInfo(context)
        return info != null && info.isConnectedOrConnecting
    }
}

