package com.example.loginapp.repository
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.loginapp.data.Resource
import com.example.loginapp.models.Profile
import com.example.loginapp.network.AuthApi
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
class LoginRepository @Inject constructor(private val api: AuthApi, db:LoginDatabase, application: Application){
    private val profileDao: ProfileDao
    private val context: Context

    init {
        context=application.applicationContext
        profileDao=db.profileDao()
    }
    suspend fun loginMember(email: String,password:String): Resource<ProfileData> {
        return try {
            Resource.Loading(data = true)
            val response = api.loginMember(email, password)
            Resource.Loading(data = false)
            if(!response.error){
                Log.d("Response", "ProfileData: ${response.profile}")
            Resource.Success(data = response)
            }
            else {
            Resource.Error(message =response.message)
            }
        }catch (exception: Exception) {
            Log.d("ErrorResponse", "Error: ${exception.message.toString()}")
            Resource.Error(message = exception.message.toString())


        }
    }
    suspend fun registerMember(email: String,password:String): Resource<ProfileData> {
        return try {
            Resource.Loading(data = true)
            val response = api.registerMember(email, password)
            Resource.Loading(data = false)
            if (!response.error) {
                Log.d("Response", "ProfileData: ${response.profile}")
                Resource.Success(data = response)
            } else {
                Resource.Error(message = response.message)
            }
        } catch (exception: Exception) {
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
        resultLiveData.value = Resource.Loading()

        try {
            val profileLiveData = profileDao.getProfile()
            profileLiveData.observeForever { profile ->
                profile?.let {
                    resultLiveData.value = Resource.Success(profile)
                } ?: run {
                    resultLiveData.value = Resource.Error("Profile not found")
                }
            }
        } catch (exception: Exception) {
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