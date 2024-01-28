package com.example.loginapp.network
import com.example.loginapp.models.ProfileData
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface ReceiptApi {
    @GET("signinMember.php")
    suspend fun loginMember(
        @Query("mobile_number") mobileNumber: String,
        @Query("password") password: String
    ): ProfileData
}