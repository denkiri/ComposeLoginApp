package com.example.loginapp.network
import com.example.loginapp.models.ProfileData
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton
@Singleton
interface AuthApi {
    @GET("signinMember.php")
    suspend fun loginMember(
        @Query("email") email: String,
        @Query("password") password: String
    ): ProfileData
    @GET("signupMember.php")
    suspend fun registerMember(
        @Query("email") email: String,
        @Query("password") password: String
    ): ProfileData
}