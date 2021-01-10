package com.example.myapplication

import android.app.Application
import android.util.Log
import com.example.myapplication.application.MyApp
import com.example.myapplication.data.Constants
import com.example.myapplication.network.SocketService
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.engineio.client.transports.WebSocket


class SocketManager(application : Application) {

    private val LOG_KEY = "SOCKET_MANAGER"

    var mSocket: Socket
    private val events = HashMap<String, ArrayList<(Array<Any>) -> Unit>>()

    init {
        val options = IO.Options()
        options.transports = arrayOf(WebSocket.NAME)
        options.forceNew = true

        mSocket = IO.socket(MyApp.pref.serverAddress, options)
        SocketService.makeNotification(application.applicationContext, Constants.CONNECTING_KEY)
        addEvent(Socket.EVENT_CONNECT_ERROR) {
            it.forEach { error ->
                Log.e(LOG_KEY, error.toString())
            }
        }
        addEvent(Socket.EVENT_CONNECT) {
            Log.i(LOG_KEY, "Socket connected!")
            SocketService.makeNotification(application.applicationContext, Constants.CONNECTED_KEY)
        }
        addEvent(Socket.EVENT_DISCONNECT) {
            Log.i(LOG_KEY, "Socket disconnected")
            SocketService.makeNotification(application.applicationContext, Constants.DISCONNECTED_KEY)
        }

        mSocket.connect()
    }

    fun destroy() {
        mSocket.disconnect()
        events.clear()
        instance = null
    }

    fun addEvent(name: String, callback: (Array<Any>) -> Unit) {
        if (events[name] == null) events[name] = arrayListOf()
        events[name]!!.add(callback)
        refreshEvents()
    }
    fun emit(name : String, msg : Any) {
        mSocket.emit(name, msg)
    }
    fun listen(name : String): Any{
        return mSocket.listeners(name)
    }
    fun removeEvent(name: String) {
        mSocket.off(name)
        events.remove(name)
        refreshEvents()
    }

    private fun refreshEvents() {
        events.keys.forEach {
            mSocket.off(it)
            events[it]!!.forEach { event ->
                mSocket.on(it, event)
            }
        }
        Log.i(LOG_KEY, "Refresh Finish")
    }

    companion object {
        @Volatile
        private var instance: SocketManager? = null
        lateinit var application: Application
        @JvmStatic
        fun getInstance(application : Application): SocketManager =
                instance ?: synchronized(this) {
                    instance ?: SocketManager(application).also {
                        this.application = application
                        instance = it
                    }
                }

    }
}