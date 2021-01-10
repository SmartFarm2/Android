package com.example.myapplication.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.SocketManager
import com.example.myapplication.data.Constants
import com.example.myapplication.data.WeatherData
import com.example.myapplication.network.RetrofitClient
import com.example.myapplication.network.RetrofitService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class MainActivityViewModel(startingTemp: Int) : ViewModel() {

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

    private var weather = MutableLiveData<String>()
    val weatherData: LiveData<String>
        get() = weather

    private val retrofit: Retrofit = RetrofitClient.getInstance()
    private var weatherService: RetrofitService

    init {
        weatherService = retrofit.create(RetrofitService::class.java)

        temp.value = startingTemp
        hum.value = startingTemp
        cycle.value = true
        door.value = true
        weather.value = "맑음"
    }

    internal fun getTemp(application: Application) {
        SocketManager.getInstance(application).addEvent(Constants.SOCKET_TEMP) {
            CoroutineScope(Dispatchers.Main).launch {
                temp.value = it[0] as Int
                hum.value = it[0] as Int
            }
        }
    }

    internal fun getCycle(application: Application) {
        SocketManager.getInstance(application).addEvent(Constants.SOCKET_CYCLE) {
            CoroutineScope(Dispatchers.Main).launch {
                cycle.value = it[0] as Boolean
            }
        }
    }

    internal fun getDoor(application: Application) {
        SocketManager.getInstance(application).addEvent(Constants.SOCKET_DOOR) {
            CoroutineScope(Dispatchers.Main).launch {
                door.value = it[0] as Boolean
            }
        }
    }

    internal fun setCycle(application: Application) {
        SocketManager.getInstance(application).emit(Constants.SOCKET_CYCLE_CHANGE, cycle)
        cycle.value = cycle.value?.not()
    }

    internal fun setDoor(application: Application) {
        SocketManager.getInstance(application).emit(Constants.SOCKET_DOOR_CHANGE, "1, $door")
        door.value = door.value?.not()
    }

    internal fun getWeather(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            //retrofit
            weatherService.getWeather()
                .enqueue(object : Callback<WeatherData> {
                    override fun onResponse(
                        call: Call<WeatherData>,
                        response: Response<WeatherData>
                    ) {
                        weather.value = response.body()?.weather
                        Log.d("weather", "data: ${response.body()}")
                    }

                    override fun onFailure(call: Call<WeatherData>, t: Throwable) {
                        Toast.makeText(context, "날씨 정보를 받아오지 못하였습니다.", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }
}
