package com.smartfarm.myapplication.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.smartfarm.myapplication.SocketManager
import com.smartfarm.myapplication.data.Constants
import com.smartfarm.myapplication.data.WeatherData
import com.smartfarm.myapplication.network.RetrofitClient
import com.smartfarm.myapplication.network.RetrofitService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class CCTVActivityViewModel(startingTemp: Int, application: Application) : ViewModel() {

    var manager : SocketManager

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

    init {
        cycle.value = true
        door.value = true
        secondDoor.value = true

        manager = SocketManager.getInstance(application)
    }

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
                val array = it[0].toString().split(",").map { it == "True" }.toList()
                door.value = array[0]
                secondDoor.value = array[1]
            }
        }
    }

    internal fun setCycle() {
        manager.emit(Constants.SOCKET_CYCLE_CHANGE, cycle.value!!.not())
        cycle.value = cycle.value?.not()
    }

    internal fun setDoor() {
        manager.emit(Constants.SOCKET_DOOR_CHANGE, "1, ${door.value?.not()}")
        door.value = door.value?.not()
    }
}
