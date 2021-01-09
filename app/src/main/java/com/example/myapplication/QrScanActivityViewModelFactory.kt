package com.example.myapplication

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class QrScanActivityViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QrScanActivityViewModel::class.java)){
            return QrScanActivityViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown View Model Class")
    }


}