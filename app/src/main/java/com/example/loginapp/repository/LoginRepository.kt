package com.example.loginapp.repository
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.loginapp.data.Resource
import com.example.loginapp.models.Profile
import com.example.loginapp.network.ReceiptApi
import com.example.loginapp.models.ProfileData
import com.example.loginapp.storage.LoginDatabase
import com.example.loginapp.storage.daos.ProfileDao
import com.example.loginapp.storage.getLoginStatus
import com.example.loginapp.storage.setLoginStatus
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject
class LoginRepository @Inject constructor(private val api: ReceiptApi,db:LoginDatabase,application: Application){
    private val profileDao: ProfileDao
    private val context: Context

    init {
        context=application.applicationContext
        profileDao=db.profileDao()
    }
    suspend fun loginMember(mobileNumber: String,password:String): Resource<ProfileData> {
        return try {
            Resource.Loading(data = true)
            val response = api.loginMember(mobileNumber, password)
            if(!response.error){
                Log.d("Response", "ProfileData: ${response.profile}")

            }
            Resource.Loading(data = false)
            Resource.Success(data = response)

        }catch (exception: Exception) {
            Log.d("ErrorResponse", "Error: ${exception.message.toString()}")
            Resource.Error(message = exception.message.toString())


        }
    }
    @OptIn(DelicateCoroutinesApi::class)
    fun saveProfile(data: ProfileData) {
        GlobalScope.launch(context = Dispatchers.IO) {
            profileDao.delete()
            data.profile.let { profileDao.insertProfile(it) }
        }
    }

    fun getProfile(): LiveData<Resource<Profile>> {
        val resultLiveData = MutableLiveData<Resource<Profile>>()
        resultLiveData.value = Resource.Loading() // Notify UI that data loading is in progress

        try {
            // Fetch profile data asynchronously
            val profileLiveData = profileDao.getProfile()

            // Observe the profileLiveData to handle different states
            profileLiveData.observeForever { profile ->
                // Check if the profile data is not null
                profile?.let {
                    resultLiveData.value = Resource.Success(profile) // Notify UI with success and provide the profile data
                } ?: run {
                    resultLiveData.value = Resource.Error("Profile not found") // Notify UI with error if profile data is null
                }
            }
        } catch (exception: Exception) {
            // Handle exceptions if any
            resultLiveData.value = Resource.Error("Error: ${exception.message}")
        }

        return resultLiveData
    }



    suspend fun setLoginStatus(isLoggedIn: Boolean): Flow<Resource<Unit>> = flow {
        try {
            context.setLoginStatus(isLoggedIn)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(message = e.message, data = null))
        }
    }

    suspend fun getLoginStatus(): Flow<Resource<Boolean?>> = flow {
        try {
            val loginStatus = context.getLoginStatus()
            emit(Resource.Success(loginStatus))
        } catch (e: Exception) {
            emit(Resource.Error(message = e.message, data = null))
        }
    }



}