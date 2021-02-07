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

    private var pump = MutableLiveData<Boolean>()
    val pumpData: LiveData<Boolean>
        get() = pump

    private var insideHum = MutableLiveData<Int>()
    val insideHumData: LiveData<Int>
        get() = insideHum

    private var cycle = MutableLiveData<Boolean>()
    val cycleData: LiveData<Boolean>
        get() = cycle

    private var door = MutableLiveData<Int>()
    val doorData: LiveData<Int>
        get() = door

    private var door2 = MutableLiveData<Int>()
    val doorData2: LiveData<Int>
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

    private var voltage220 = MutableLiveData<Boolean>()
    val voltageData220: LiveData<Boolean>
        get() = voltage220

    private var pumpAuto = MutableLiveData<Boolean>()
    val pumpAutoData: LiveData<Boolean>
        get() = pumpAuto

    private var doorAuto1 = MutableLiveData<Boolean>()
    val doorAutoData1: LiveData<Boolean>
        get() = doorAuto1

    private var doorAuto2 = MutableLiveData<Boolean>()
    val doorAutoData2: LiveData<Boolean>
        get() = doorAuto2

    private var auto24 = MutableLiveData<Boolean>()
    val auto24Data: LiveData<Boolean>
        get() = auto24

    private var auto220 = MutableLiveData<Boolean>()
    val auto220Data: LiveData<Boolean>
        get() = auto220

    private val retrofit: Retrofit = RetrofitClient.getInstance()
    private var weatherService: RetrofitService

    init {
        temp.value = "20℃"
        hum.value = startingTemp
        insideTemp.value = startingTemp
        insideHum.value = startingTemp
        cycle.value = true
        door.value = 0
        weather.value = "맑음"

        weatherService = retrofit.create(RetrofitService::class.java)
        manager = SocketManager.getInstance(application)
        manager.emit(Constants.INIT, "init")
    }

    fun observing() {
        manager.socketClear()
        getTemp()
        getHumi()
        getCycle()
        getDoor()
        getDoor2()
        getInsideTemp()
        getInsideHumi()
        getVoltage()
        getPump()
        get220Voltage()
        getIsAuto()
    }

    fun deobserving() {
        manager.removeEvent(Constants.SOCKET_DOOR)
        manager.removeEvent(Constants.SOCKET_DOOR2)
        manager.removeEvent(Constants.SOCKET_CYCLE)
        manager.removeEvent(Constants.SOCKET_TEMP)
        manager.removeEvent(Constants.SOCKET_HUMI)
        manager.removeEvent(Constants.SOCKET_TEMP_INSIDE)
        manager.removeEvent(Constants.SOCKET_HUMI_INSIDE)
        manager.removeEvent(Constants.SOCKET_VOLTAGE)
        manager.removeEvent(Constants.SOCKET_220VOLTAGE)
        manager.removeEvent(Constants.SOCKET_220VOLTAGE_AUTO)
        manager.removeEvent(Constants.SOCKET_DOOR2_AUTO)
        manager.removeEvent(Constants.SOCKET_PUMP_AUTO)
        manager.removeEvent(Constants.SOCKET_VOLTAGE_AUTO)
        manager.removeEvent(Constants.AUTO)
    }

    private fun getVoltage() {
        manager.addEvent(Constants.SOCKET_VOLTAGE) {
            CoroutineScope(Dispatchers.Main).launch {
                voltage.value = it[0] as Boolean
            }
        }
    }

    private fun getIsAuto() {
        manager.addEvent(Constants.SOCKET_PUMP_AUTO) {
            CoroutineScope(Dispatchers.Main).launch {
                pumpAuto.value = it[0] as Boolean
            }
        }
        manager.addEvent(Constants.AUTO) {
            CoroutineScope(Dispatchers.Main).launch {
                doorAuto1.value = it[0] as Boolean
            }
        }
        manager.addEvent(Constants.SOCKET_DOOR2_AUTO) {
            CoroutineScope(Dispatchers.Main).launch {
                doorAuto2.value = it[0] as Boolean
            }
        }
        manager.addEvent(Constants.SOCKET_VOLTAGE_AUTO) {
            CoroutineScope(Dispatchers.Main).launch {
                auto24.value = it[0] as Boolean
            }
        }
        manager.addEvent(Constants.SOCKET_220VOLTAGE_AUTO) {
            CoroutineScope(Dispatchers.Main).launch {
                auto220.value = it[0] as Boolean
            }
        }
    }

    private fun get220Voltage() {
        manager.addEvent(Constants.SOCKET_220VOLTAGE) {
            CoroutineScope(Dispatchers.Main).launch {
                voltage220.value = it[0] as Boolean
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
                door.value = it[0] as Int
            }
        }
    }

    private fun getDoor2() {
        manager.addEvent(Constants.SOCKET_DOOR2) {
            CoroutineScope(Dispatchers.Main).launch {
                door2.value = it[0] as Int
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

    internal fun setPump() {
        if (pump.value == false) {
            manager.emit(Constants.SOCKET_PUMP, true)
        } else if (pump.value == true) {
            manager.emit(Constants.SOCKET_PUMP, false)
        }
    }

    internal fun setDoor() {
        if (door.value == Constants.CLOCK) {
            manager.emit(Constants.SOCKET_DOOR, Constants.UNCLOCK)
        } else if (door.value == Constants.UNCLOCK) {
            manager.emit(Constants.SOCKET_DOOR, Constants.CLOCK)
        } else {
            manager.emit(Constants.SOCKET_DOOR, Constants.CLOCK)
        }
    }

    internal fun setDoor2() {
        if (door2.value == Constants.CLOCK) {
            manager.emit(Constants.SOCKET_DOOR2, Constants.UNCLOCK)
        } else if (door2.value == Constants.UNCLOCK) {
            manager.emit(Constants.SOCKET_DOOR2, Constants.CLOCK)
        } else {
            manager.emit(Constants.SOCKET_DOOR2, Constants.CLOCK)
        }
    }

    internal fun setVoltage() {
        if (voltage.value == false) {
            manager.emit(Constants.SOCKET_VOLTAGE, true)
        } else if (voltage.value == true) {
            manager.emit(Constants.SOCKET_VOLTAGE, false)
        }
    }

    internal fun set220Voltage() {
        if (voltage220.value == false) {
            manager.emit(Constants.SOCKET_220VOLTAGE, true)
        } else if (voltage220.value == true) {
            manager.emit(Constants.SOCKET_220VOLTAGE, false)
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
