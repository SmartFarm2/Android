package com.smartfarm.myapplication.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.smartfarm.myapplication.network.SocketManager
import com.smartfarm.myapplication.data.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CCTVActivityViewModel(application: Application) : ViewModel() {

    private var manager : SocketManager = SocketManager.getInstance(application)

    private var cycle = MutableLiveData<Boolean>()
    val cycleData: LiveData<Boolean>
        get() = cycle

    private var door = MutableLiveData<Boolean>()
    val doorData: LiveData<Boolean>
        get() = door

    private var secondDoor = MutableLiveData<Boolean>()
    val secondDoorData: LiveData<Boolean>
        get() = secondDoor

    private var _toasts = MutableLiveData<Event<String>>()
    val toasts : LiveData<Event<String>>
    get() = _toasts

    fun observing() {
        getCycle()
        getDoor()
    }

    fun deobserving() {
        manager.removeEvent(Constants.SOCKET_DOOR)
        manager.removeEvent(Constants.SOCKET_CYCLE)
    }

    private fun getCycle() {
        manager.addEvent(Constants.SOCKET_CYCLE) {
            CoroutineScope(Dispatchers.Main).launch {
                cycle.value = it[0] == "True"
            }
        }
    }

    private fun getDoor() {
        manager.addEvent(Constants.SOCKET_DOOR) {
            CoroutineScope(Dispatchers.Main).launch {
                Log.d("door2", it[0].toString())
                door.value = it[0] as Boolean
                Log.d("door", "data: ${door.value}")
            }
        }
    }
}
