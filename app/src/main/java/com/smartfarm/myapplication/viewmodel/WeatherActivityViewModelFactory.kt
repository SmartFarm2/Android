package com.smartfarm.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class WeatherActivityViewModelFactory(private val application: Application) : ViewModelProvider.Factory{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherActivityViewModel::class.java)){
            return WeatherActivityViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown View Model Class")
    }


}