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

class WeatherActivityViewModel(application: Application) : ViewModel() {


    private var weather = MutableLiveData<String>()
    val weatherData: LiveData<String>
        get() = weather

    private var _toasts = MutableLiveData<Event<String>>()
    val toasts: LiveData<Event<String>>
        get() = _toasts

    private val retrofit: Retrofit = RetrofitClient.getInstance()
    private var weatherService: RetrofitService

    init {
        weatherService = retrofit.create(RetrofitService::class.java)

        weather.value = "맑음"
    }

    internal fun getWeather() {
        CoroutineScope(Dispatchers.IO).launch {
            //retrofit
            weatherService.getWeather()
                .enqueue(object : Callback<WeatherData> {
                    override fun onResponse(
                        call: Call<WeatherData>,
                        response: Response<WeatherData>
                    ) {
                        if (response.code() == 200) {
                            weather.value = response.body()?.weather
                        } else {
                            weather.value = "정보 없음"
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
