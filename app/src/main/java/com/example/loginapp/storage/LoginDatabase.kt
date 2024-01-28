package com.example.loginapp.storage
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.loginapp.models.Profile
import com.example.loginapp.storage.daos.ProfileDao
import javax.inject.Singleton

@Singleton
@Database(entities = [Profile::class],version = 1,exportSchema = false)
 abstract class LoginDatabase :RoomDatabase() {
    abstract fun profileDao(): ProfileDao
 }
