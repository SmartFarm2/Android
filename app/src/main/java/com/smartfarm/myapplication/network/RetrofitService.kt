package com.smartfarm.myapplication.network

import com.smartfarm.myapplication.data.Constants
import com.smartfarm.myapplication.data.WeatherData
import retrofit2.Call
import retrofit2.http.GET

interface RetrofitService {

    //날씨 부분
    @GET(Constants.WEATHER_URL)
    fun getWeather(
    ): Call<WeatherData>


}