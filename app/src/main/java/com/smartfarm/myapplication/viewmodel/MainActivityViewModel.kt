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

    private var light = MutableLiveData<Boolean>()
    val lightData: LiveData<Boolean>
        get() = light

    private var pump = MutableLiveData<Boolean>()
    val pumpData: LiveData<Boolean>
        get() = pump

    private var insideHum = MutableLiveData<Int>()
    val insideHumData: LiveData<Int>
        get() = insideHum

    private var cycle = MutableLiveData<Boolean>()
    val cycleData: LiveData<Boolean>
        get() = cycle

    private var door = MutableLiveData<Boolean>()
    val doorData: LiveData<Boolean>
        get() = door

    private var door2 = MutableLiveData<Boolean>()
    val doorData2: LiveData<Boolean>
        get() = door2

    private var _toasts = MutableLiveData<Event<String>>()
    val toasts: LiveData<Event<String>>
        get() = _toasts

    private var weather = MutableLiveData<String>()
    val weatherData: LiveData<String>
        get() = weather

    private var sky = MutableLiveData<String>()
    val skyData: LiveData<String>
        get() = sky

    private var voltage = MutableLiveData<Boolean>()
    val voltageData: LiveData<Boolean>
        get() = voltage

    private val retrofit: Retrofit = RetrofitClient.getInstance()
    private var weatherService: RetrofitService

    init {
        temp.value = "20℃"
        hum.value = startingTemp
        insideTemp.value = startingTemp
        insideHum.value = startingTemp
        cycle.value = true
        door.value = true
        weather.value = "맑음"

        weatherService = retrofit.create(RetrofitService::class.java)
        manager = SocketManager.getInstance(application)
    }

    fun observing() {
        manager.socketClear()
        getTemp()
        getHumi()
        getCycle()
        getDoor()
        getInsideTemp()
        getInsideHumi()
        getVoltage()
        getLight()
        getPump()
    }

    fun deobserving() {
        manager.removeEvent(Constants.SOCKET_DOOR)
        manager.removeEvent(Constants.SOCKET_CYCLE)
        manager.removeEvent(Constants.SOCKET_TEMP)
        manager.removeEvent(Constants.SOCKET_HUMI)
        manager.removeEvent(Constants.SOCKET_TEMP_INSIDE)
        manager.removeEvent(Constants.SOCKET_HUMI_INSIDE)
        manager.removeEvent(Constants.SOCKET_VOLTAGE)
    }

    private fun getVoltage() {
        manager.addEvent(Constants.SOCKET_VOLTAGE) {
            CoroutineScope(Dispatchers.Main).launch {
                voltage.value = it[0] as Boolean
            }
        }
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
                Log.d("door2", it[0].toString())
                door.value = it[0] as Boolean
                Log.d("door", "data: ${door.value}")
            }
        }
    }

    private fun getLight() {
        manager.addEvent(Constants.SOCKET_LIGHT) {
            CoroutineScope(Dispatchers.Main).launch {
                light.value = it[0] as Boolean
            }
        }
    }

    private fun getPump() {
        manager.addEvent(Constants.SOCKET_PUMP) {
            CoroutineScope(Dispatchers.Main).launch {
                pump.value = it[0] as Boolean
            }
        }
    }

    internal fun setCycle() {
        manager.emit(Constants.SOCKET_CYCLE_CHANGE, cycle.value!!.not())
        cycle.value = cycle.value?.not()
    }

    internal fun setLight() {
        if (door.value == false) {
            manager.emit(Constants.SOCKET_LIGHT, false)
        } else if (door.value == true) {
            manager.emit(Constants.SOCKET_LIGHT, true)
        }
    }

    internal fun setPump() {
        if (door.value == false) {
            manager.emit(Constants.SOCKET_PUMP, false)
        } else if (door.value == true) {
            manager.emit(Constants.SOCKET_PUMP, true)
        }
    }

    internal fun setDoor() {
        if (door.value == false) {
            manager.emit(Constants.SOCKET_DOOR, false)
        } else if (door.value == true) {
            manager.emit(Constants.SOCKET_DOOR, true)
        }
    }

    internal fun setVoltage() {
        if (voltage.value == false) {
            manager.emit(Constants.SOCKET_VOLTAGE, false)
        } else if (voltage.value == true) {
            manager.emit(Constants.SOCKET_VOLTAGE, true)
        }
    }

    internal fun getWeather() {
        CoroutineScope(Dispatchers.IO).launch {
            //retrofit
            weatherService.getWeather()
                .enqueue(object : Callback<WeatherData> {
                    override fun onResponse(
                        call: Call<WeatherData>,
                        response: Response<WeatherData>,
                    ) {

                        when (response.code()) {
                            200 -> {
                                weather.value = response.body()?.weather
                                sky.value = response.body()?.sky
                            }
                            else -> {
                                weather.value = "정보 없음"
                                sky.value = "정보 없음"
                            }
                        }
                        Log.d("weather", "data: ${response.body()}")
                    }

                    override fun onFailure(call: Call<WeatherData>, t: Throwable) {
                        _toasts.value = Event("날씨 정보를 받아오지 못하였습니다.");
                        //Toast.makeText(context, "날씨 정보를 받아오지 못하였습니다.", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }
}
