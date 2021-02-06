package com.smartfarm.myapplication.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.smartfarm.myapplication.data.Constants
import com.smartfarm.myapplication.network.SocketManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SetPlantTempViewModel(application: Application) : ViewModel() {

    private var manager : SocketManager = SocketManager.getInstance(application)

    var plantTemp = MutableLiveData<String>()
    var plantTemp2 = MutableLiveData<String>()

    private var _toasts = MutableLiveData<Event<String>>()
    val toasts : LiveData<Event<String>>
        get() = _toasts

    private var status = MutableLiveData<Int>()
    val statusData : LiveData<Int>
        get() = status

    init {
        status.value = 0
    }

    fun observing() {
        getSetTempState()
    }

    fun deobserving() {
        manager.removeEvent(Constants.SOCKET_SET_TEMP)
        manager.removeEvent(Constants.SOCKET_SET_TEMP2)
    }

    private fun getSetTempState(){
        manager.addEvent(Constants.SOCKET_SET_TEMP) {
            CoroutineScope(Dispatchers.Main).launch {
                Log.d("TAG", "통과2")
                status.value = status.value?.plus(1)
            }
        }

    }

    fun setTemp(){
        if(plantTemp.value.isNullOrBlank()) {
            _toasts.value = Event("온도값을 설정해 주십시오.")
        }
        else{
            manager.emit(Constants.SOCKET_SET_TEMP, plantTemp.value.toString())
        }
    }
}