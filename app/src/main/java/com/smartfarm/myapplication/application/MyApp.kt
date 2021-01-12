package com.smartfarm.myapplication.application

import android.app.Application
import android.content.Intent
import android.os.Build
import com.smartfarm.myapplication.data.Constants
import com.smartfarm.myapplication.database.SharedPreferenceData
import com.smartfarm.myapplication.network.SocketService

class MyApp : Application() {
    companion object {
        lateinit var pref: SharedPreferenceData
    }

    override fun onCreate() {
        super.onCreate()
        pref = SharedPreferenceData(this)
        if(pref.serverAddress == Constants.SERVER_ADDRESS) return; // If server address is not set.
        val intent = Intent(this, SocketService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }

    }
}