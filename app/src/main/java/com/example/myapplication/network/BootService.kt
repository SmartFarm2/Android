package com.example.myapplication.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.myapplication.application.MyApp
import com.example.myapplication.data.Constants

class BootService : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if(MyApp.pref.serverAddress == Constants.SERVER_ADDRESS) return; // If server address is not set.
        val i = Intent(context, SocketService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context?.startForegroundService(i);
        } else {
            context?.startService(i)
        }
    }
}