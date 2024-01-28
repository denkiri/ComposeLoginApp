package com.example.loginapp.di

import android.content.Context
import androidx.room.Room
import com.example.loginapp.storage.LoginDatabase
import com.example.loginapp.utils.Constants.LOGIN_DATABASE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): LoginDatabase {
        return Room.databaseBuilder(
            context,
            LoginDatabase::class.java,
            LOGIN_DATABASE
        ).build()
    }

//    @Provides
//    @Singleton
//    fun provideLocalDataSource(
//        database:  MembersAppDatabase
//    ): LocalDataSource {
//        return LocalDataSourceImpl(
//            borutoDatabase = database
//        )
//    }

}