package com.smartfarm.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class SetPlantViewModelFactory(private val application: Application) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SetPlantTempViewModel::class.java)){
            return SetPlantTempViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown View Model Class")
    }

}