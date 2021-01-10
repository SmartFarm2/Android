package com.example.myapplication.application

import android.app.Application
import android.content.Intent
import android.os.Build
import com.example.myapplication.database.SharedPreferenceData
import com.example.myapplication.network.SocketService

class MyApp : Application() {
    companion object {
        lateinit var pref: SharedPreferenceData
    }

    override fun onCreate() {
        super.onCreate()
        pref = SharedPreferenceData(this)
        val intent = Intent(this, SocketService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }

    }
}