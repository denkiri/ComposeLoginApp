package com.example.loginapp.storage
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
val Context.loginStatus: DataStore<Preferences> by preferencesDataStore(name = "login_status")

object DataStoreKeys {
    val LOGIN_STATUS = booleanPreferencesKey("login_status")
}

suspend fun Context.getLoginStatus(): Boolean? {
    return loginStatus.data.map { preferences ->
        preferences[DataStoreKeys.LOGIN_STATUS] ?: false
    }.firstOrNull()
}

suspend fun Context.setLoginStatus(isLoggedIn: Boolean) {
    loginStatus.edit { preferences ->
        preferences[DataStoreKeys.LOGIN_STATUS] = isLoggedIn
    }
}
