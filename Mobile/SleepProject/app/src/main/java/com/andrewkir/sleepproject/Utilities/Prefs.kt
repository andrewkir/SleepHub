package com.andrewkir.sleepproject.Utilities

import android.content.Context
import android.content.SharedPreferences

class Prefs(context: Context) {

    val PREFS_FILENAME = "prefs"
    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0)
    val IS_LOGGED_IN = "isLoggedIn"
    val TOKEN = "UserToken"
    val USERNAME = "UserUserName"
    val NAME = "UserName"
    var isLoggedIn: Boolean
        get() = prefs.getBoolean(IS_LOGGED_IN, false)
        set(value) {
            prefs.edit().putBoolean(IS_LOGGED_IN, value).apply()
            if(!value){
                prefs.edit().putString(TOKEN, "").apply()
                prefs.edit().putString(USERNAME, "").apply()
            }
        }
    var userToken: String
        get() = prefs.getString(TOKEN, "")
        set(value) = prefs.edit().putString(TOKEN, value).apply()
    var userUsername: String
        get() = prefs.getString(USERNAME, "")
        set(value) = prefs.edit().putString(USERNAME, value).apply()
    var userName: String
        get() = prefs.getString(NAME, "")
        set(value) = prefs.edit().putString(NAME, value).apply()
}