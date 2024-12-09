package com.example.capstone

import android.content.Context
import android.content.SharedPreferences
import com.example.capstone.response.Data

class UserPreferences(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

    companion object {
        private const val TOKEN_KEY = "TOKEN_KEY"
        private const val USER_ID_KEY = "USER_ID_KEY"
        private const val USERNAME_KEY = "USERNAME_KEY"
        private const val NAME_KEY = "NAME_KEY"
        private const val CREATED_AT_KEY = "CREATED_AT_KEY"
    }

    // Menyimpan Data (token dan informasi user)
    fun saveUserData(data: Data) {
        val editor = sharedPreferences.edit()
        editor.putString(TOKEN_KEY, data.token)
        editor.putString(USER_ID_KEY, data.user?.id)
        editor.putString(USERNAME_KEY, data.user?.username)
        editor.putString(NAME_KEY, data.user?.name)
        editor.putString(CREATED_AT_KEY, data.user?.createdAt)
        editor.apply()
    }

    // Mengambil token
    fun getToken(): String? {
        return sharedPreferences.getString(TOKEN_KEY, null)
    }

    // Mengambil informasi pengguna
    fun getUserId(): String? {
        return sharedPreferences.getString(USER_ID_KEY, null)
    }

    fun getUsername(): String? {
        return sharedPreferences.getString(USERNAME_KEY, null)
    }

    fun getUserName(): String? {
        return sharedPreferences.getString(NAME_KEY, null)
    }

    fun getCreatedAt(): String? {
        return sharedPreferences.getString(CREATED_AT_KEY, null)
    }

    // Menghapus token dan data pengguna
    fun clearUserData() {
        val editor = sharedPreferences.edit()
        editor.remove(TOKEN_KEY)
        editor.remove(USER_ID_KEY)
        editor.remove(USERNAME_KEY)
        editor.remove(NAME_KEY)
        editor.remove(CREATED_AT_KEY)
        editor.apply()
    }
}
