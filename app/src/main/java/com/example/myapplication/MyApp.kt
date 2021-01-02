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
        SocketManager.getInstance().addEvent(Socket.EVENT_CONNECT) {
            SocketService.makeNotification(this, Constants.CONNECTED_KEY)
        }
        SocketManager.getInstance().addEvent(Socket.EVENT_DISCONNECT) {
            SocketService.makeNotification(this, Constants.DISCONNECTED_KEY)
        }
    }
}