package com.smartfarm.myapplication.network

import android.util.Log
import com.smartfarm.myapplication.application.MyApp
import com.smartfarm.myapplication.data.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private var instance : Retrofit? = null

    fun getInstance() : Retrofit {
        if(instance == null){
            val logging = HttpLoggingInterceptor() {
                Log.d("OKHTTP", it)
            };
            var okHttpClient = OkHttpClient.Builder()
                .addInterceptor(logging)
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .build()

            instance = Retrofit.Builder()
                .baseUrl(MyApp.pref.serverAddress + ":8000")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return instance!!
    }
}