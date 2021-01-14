package com.smartfarm.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class CCTVActivityViewModelFactory(private val startingTotal: Int, private val application: Application) : ViewModelProvider.Factory{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CCTVActivityViewModel::class.java)){
            return CCTVActivityViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown View Model Class")
    }


}