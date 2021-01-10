package com.example.myapplication.network

import com.example.myapplication.data.Constants
import com.example.myapplication.data.WeatherData
import retrofit2.Call
import retrofit2.http.GET

interface RetrofitService {

    //날씨 부분
    @GET(Constants.WEATHER_URL)
    fun getWeather(
    ): Call<WeatherData>


}