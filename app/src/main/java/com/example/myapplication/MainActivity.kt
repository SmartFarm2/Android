package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.Constants
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val LOG_KEY = "MAIN_LOG"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var bool = false;
        setContentView(R.layout.activity_main)
        SocketManager.getInstance(application).addEvent(Constants.SOCKET_DOOR) {
            val temp = it.get(0)
            CoroutineScope(Dispatchers.Main).launch {
                test.text = "$temp"
                //SocketService.makeNotification(this@MainActivity, Constants.CONNECTED_KEY)
            }
        }
        button.setOnClickListener {
            SocketManager.getInstance(application).emit(Constants.SOCKET_DOOR_CHANGE, "1,$bool")
            bool = !bool
        }
    }

    override fun onDestroy() {
        SocketManager.getInstance(application).removeEvent(Constants.SOCKET_TEMP)
        super.onDestroy()
    }
}