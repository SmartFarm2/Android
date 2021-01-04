package com.example.myapplication

import android.app.Application
import android.content.Intent
import android.os.Build
import com.example.myapplication.Constants
import io.socket.client.Socket

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