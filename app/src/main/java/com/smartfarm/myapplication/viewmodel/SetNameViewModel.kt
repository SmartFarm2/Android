package com.smartfarm.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.smartfarm.myapplication.application.MyApp
import com.smartfarm.myapplication.data.Constants
import com.smartfarm.myapplication.network.SocketManager

class SetNameViewModel() : ViewModel(){

    private var _toasts = MutableLiveData<Event<String>>()
    val toasts : LiveData<Event<String>>
        get() = _toasts

    var name = MutableLiveData<String>()

    fun setName(){
        if(name.value.isNullOrEmpty())
        {
            _toasts.value = Event("이름은 공백일수 없습니다.")
        }else{
            MyApp.pref.name = name.value.toString()
            _toasts.value = Event(Constants.NAME_VERIFY_KEY)
        }
    }

}