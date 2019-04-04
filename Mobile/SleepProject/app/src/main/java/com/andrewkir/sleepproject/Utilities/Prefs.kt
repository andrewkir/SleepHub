package com.andrewkir.sleepproject.Utilities

import android.content.Context
import android.content.SharedPreferences

class Prefs(context: Context) {

    val PREFS_FILENAME = "prefs"
    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0)
    val IS_LOGGED_IN = "isLoggedIn"
    val USER_PASS = "password"
    val USER_EMAIL = "userEmail"
    var isLoggedIn: Boolean
        get() = prefs.getBoolean(IS_LOGGED_IN, false)
        set(value) {
            prefs.edit().putBoolean(IS_LOGGED_IN, value).apply()
            if(!value){
                prefs.edit().putString(USER_PASS, "").apply()
                prefs.edit().putString(USER_EMAIL, "").apply()
            }
        }
    var userPassword: String
        get() = prefs.getString(USER_PASS, "")
        set(value) = prefs.edit().putString(USER_PASS, value).apply()

    var userEmail: String
        get() = prefs.getString(USER_EMAIL, "")
        set(value) = prefs.edit().putString(USER_EMAIL, value).apply()
}