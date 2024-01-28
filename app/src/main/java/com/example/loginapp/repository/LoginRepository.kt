package com.example.loginapp.repository
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.loginapp.data.Resource
import com.example.loginapp.models.Profile
import com.example.loginapp.network.ReceiptApi
import com.example.loginapp.models.ProfileData
import com.example.loginapp.storage.LoginDatabase
import com.example.loginapp.storage.daos.ProfileDao
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject
class LoginRepository @Inject constructor(private val api: ReceiptApi,db:LoginDatabase){
    private val profileDao: ProfileDao
    init {
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
    fun getProfile(): LiveData<Profile> {
        return profileDao.getProfile()
    }


}