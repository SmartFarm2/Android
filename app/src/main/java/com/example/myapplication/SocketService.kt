package com.example.myapplication

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.myapplication.Constants
import com.example.myapplication.Constants.channelID
import com.example.myapplication.Constants.notificationId

class SocketService : Service() {
    private var notificationManager: NotificationManager? = null
    private lateinit var socketManager: SocketManager

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        initService()
    }

    companion object {
        fun makeNotification(context: Context, action: String) {
            val i = Intent(context, SocketService::class.java)
            i.action = action
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(i);
            } else {
                context.startService(i)
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.action?.let { action ->
            with(Messages) {
                when (action) {
                    Constants.CONNECTED_KEY -> {
                        updateAlterMessage(CONNECTED_TITLE, CONNECTED_CONTENT)
                    }
                    Constants.DISCONNECTED_KEY -> {
                        updateAlterMessage(DISCONNECTED_TITLE, DISCONNECTED_CONTENT)
                    }
                    Constants.CONNECTING_KEY -> {
                        updateAlterMessage(CONNECTING_TITLE, CONNECTING_CONTENT)
                    }
                }
            }
        }
        return START_STICKY
    }


    private fun initService() {
        createNotificationChannel()
        socketManager = SocketManager.getInstance()
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        updateAlterMessage(Messages.CONNECTING_TITLE, Messages.CONNECTING_CONTENT)
    }

    private fun makeNotification(title: String, content: String, code: Int) {
        createNotificationChannel()

        val intent2 = Intent(this, MainActivity::class.java).apply {
            putExtra("id", code)
        }
        val pendingIntent2: PendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent2,
                PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(applicationContext, channelID).apply {
            setContentTitle(title)
            setContentText(content)
            setSmallIcon(android.R.drawable.ic_dialog_info)
            setAutoCancel(true)
            priority = NotificationCompat.PRIORITY_HIGH
            setContentIntent(pendingIntent2)
        }.build()

        notificationManager?.notify(notificationId, notification)
    }

    override fun onDestroy() {
        socketManager.destroy()
        super.onDestroy()
    }

    private fun updateAlterMessage(title: String, content: String) {
        createNotificationChannel()
        val base = NotificationCompat.Builder(applicationContext, channelID).apply {
            setContentTitle(title)
            setContentText(content)
            setSmallIcon(android.R.drawable.ic_dialog_info)
            priority = NotificationCompat.PRIORITY_MIN
        }.build()
        startForeground(notificationId - 1, base)
    }

    private fun createNotificationChannel(id: String = channelID, name: String = Constants.channelName, channelDescription: String = Constants.channelDescription) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(id, name, importance)
                    .apply {
                        this.description = channelDescription
                    }
            notificationManager?.createNotificationChannel(channel)
        }
    }
}