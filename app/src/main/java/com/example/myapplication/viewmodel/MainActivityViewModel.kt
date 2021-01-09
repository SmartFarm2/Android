package com.example.myapplication.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.SocketManager
import com.example.myapplication.data.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivityViewModel(startingTemp: Int, application: Application): ViewModel() {

    private var temp = MutableLiveData<Int>()
    val tempData: LiveData<Int>
    get() = temp

    private var hum = MutableLiveData<Int>()
    val humData: LiveData<Int>
    get() = hum

    private var cycle = MutableLiveData<Boolean>()
    val cycleData: LiveData<Boolean>
        get() = cycle

    private var door = MutableLiveData<Boolean>()
    val doorData: LiveData<Boolean>
        get() = door

    init {
        temp.value = startingTemp
        hum.value = startingTemp
        cycle.value = true
        door.value = true
    }

    internal fun getTemp(application: Application){
        SocketManager.getInstance(application).addEvent(Constants.SOCKET_TEMP) {
            CoroutineScope(Dispatchers.Main).launch {
                temp.value = it[0] as Int
                hum.value = it[0] as Int
            }
        }
    }

    internal fun getCycle(application: Application){
        SocketManager.getInstance(application).addEvent(Constants.SOCKET_CYCLE) {
            CoroutineScope(Dispatchers.Main).launch {
                cycle.value = it[0] as Boolean
            }
        }
    }

    internal fun getDoor(application: Application){
        SocketManager.getInstance(application).addEvent(Constants.SOCKET_DOOR) {
            CoroutineScope(Dispatchers.Main).launch {
                door.value = it[0] as Boolean
            }
        }
    }

    internal fun setCycle(application: Application){
        SocketManager.getInstance(application).emit(Constants.SOCKET_CYCLE_CHANGE, cycle)
        cycle.value = cycle.value?.not()
    }

    internal fun setDoor(application: Application){
        SocketManager.getInstance(application).emit(Constants.SOCKET_DOOR_CHANGE, "1, $door")
        door.value = door.value?.not()
    }

}
