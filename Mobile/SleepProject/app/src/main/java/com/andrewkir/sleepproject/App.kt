package com.andrewkir.sleepproject

import android.app.Application
import com.andrewkir.sleepproject.Utilities.Prefs

class App : Application() {

    companion object {
        lateinit var prefs: Prefs
    }

    override fun onCreate() {
        prefs = Prefs(applicationContext)
        super.onCreate()
    }
}