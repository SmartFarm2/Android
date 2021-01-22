package com.smartfarm.myapplication.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.smartfarm.myapplication.network.SocketManager
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
import kotlin.math.log

class MainActivityViewModel(startingTemp: Int, application: Application) : ViewModel() {

    var manager: SocketManager

    private var temp = MutableLiveData<String>()
    val tempData: LiveData<String>
        get() = temp

    private var insideTemp = MutableLiveData<Int>()
    val insideTempData: LiveData<Int>
        get() = insideTemp

    private var hum = MutableLiveData<Int>()
    val humData: LiveData<Int>
        get() = hum

    private var insideHum = MutableLiveData<Int>()
    val insideHumData: LiveData<Int>
        get() = insideHum

    private var cycle = MutableLiveData<Boolean>()
    val cycleData: LiveData<Boolean>
        get() = cycle

    private var door = MutableLiveData<Boolean>()
    val doorData: LiveData<Boolean>
        get() = door

    private var _toasts = MutableLiveData<Event<String>>()
    val toasts: LiveData<Event<String>>
        get() = _toasts

    init {

        temp.value = "20℃"
        hum.value = startingTemp
        insideTemp.value = startingTemp
        insideHum.value = startingTemp
        cycle.value = true
        door.value = true

        manager = SocketManager.getInstance(application)
    }

    fun observing() {
        getTemp()
        getHumi()
        getCycle()
        getDoor()
        getInsideTemp()
        getInsideHumi()
    }

    fun deobserving() {
        manager.removeEvent(Constants.SOCKET_DOOR)
        manager.removeEvent(Constants.SOCKET_CYCLE)
        manager.removeEvent(Constants.SOCKET_TEMP)
        manager.removeEvent(Constants.SOCKET_HUMI)
        manager.removeEvent(Constants.SOCKET_TEMP_INSIDE)
        manager.removeEvent(Constants.SOCKET_HUMI_INSIDE)
    }

    private fun getTemp() {
        var tempLocalData = 0
        manager.addEvent(Constants.SOCKET_TEMP) {
            CoroutineScope(Dispatchers.Main).launch {
                tempLocalData = it[0] as Int
                temp.value = "$tempLocalData ℃"
            }
        }
    }

    private fun getHumi() {
        manager.addEvent(Constants.SOCKET_HUMI) {
            CoroutineScope(Dispatchers.Main).launch {
                hum.value = it[0] as Int
            }
        }
    }

    private fun getInsideTemp() {
        manager.addEvent(Constants.SOCKET_TEMP_INSIDE) {
            CoroutineScope(Dispatchers.Main).launch {
                insideTemp.value = it[0] as Int
            }
        }
    }

    private fun getInsideHumi() {
        manager.addEvent(Constants.SOCKET_HUMI_INSIDE) {
            CoroutineScope(Dispatchers.Main).launch {
                insideHum.value = it[0] as Int
            }
        }
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
                door.value = it[0] as Boolean
                Log.d("door", "data: ${door.value}")
            }
        }
    }

    internal fun setCycle() {
        manager.emit(Constants.SOCKET_CYCLE_CHANGE, cycle.value!!.not())
        cycle.value = cycle.value?.not()
    }

    internal fun setDoor() {
        if(door.value == false){
            manager.emit(Constants.SOCKET_DOOR, false)
        }else if (door.value == true){
            manager.emit(Constants.SOCKET_DOOR, true)
        }
    }
}
