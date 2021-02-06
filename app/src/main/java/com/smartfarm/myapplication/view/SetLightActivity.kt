package com.smartfarm.myapplication.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.smartfarm.myapplication.R
import com.smartfarm.myapplication.application.MyApp
import com.smartfarm.myapplication.data.Constants
import com.smartfarm.myapplication.databinding.ActivitySetLightBinding
import com.smartfarm.myapplication.network.SocketManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SetLightActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySetLightBinding
    private lateinit var manager: SocketManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_set_light)
        manager = SocketManager.getInstance(application)

        Log.d("TAG", "생겨남")

        manager.addEvent(Constants.SOCKET_START_DOOR) {
            CoroutineScope(Dispatchers.Main).launch {
                if (it[0] == binding.setLightStartHour.text.toString().toInt() * 100) {
                    if (MyApp.pref.startLight == "") {
                        startActivity(Intent(this@SetLightActivity, SetPumpActivity::class.java))
                        finish()
                    } else {
                        finish()
                    }
                    MyApp.pref.startLight =
                        (binding.setLightStartHour.text.toString().toInt() * 100).toString()
                    MyApp.pref.endLight =
                        (binding.setLightEndHour.text.toString().toInt() * 100).toString()
                    Toast.makeText(this@SetLightActivity, "설정이 완료되었습니다.", Toast.LENGTH_SHORT).show()

                    Log.d("TAG", "사라짐")
                } else {
                    Toast.makeText(this@SetLightActivity, "설정에 실패하였습니다..", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        with(binding) {
            setLightNext.setOnClickListener {
                if (!setLightStartHour.text.isNullOrEmpty()
                    && !setLightEndHour.text.isNullOrEmpty()
                    && setLightStartHour.text.toString().toInt() < 24
                    && setLightEndHour.text.toString().toInt() < 24
                    &&!setLightStartVoltage.text.isNullOrEmpty()
                    && !setLightEndVoltage.text.isNullOrEmpty()
                    && setLightStartVoltage.text.toString().toInt() < 24
                    && setLightEndVoltage.text.toString().toInt() < 24
                ) {
                    manager.emit(Constants.SOCKET_START_Light,
                        setLightStartHour.text.toString().toInt() * 100)
                    manager.emit(Constants.SOCKET_END_Light,
                        setLightEndHour.text.toString().toInt() * 100)
                    manager.emit(Constants.SOCKET_START_Voltage,
                        setLightStartVoltage.text.toString().toInt() * 100)
                    manager.emit(Constants.SOCKET_END_Voltage,
                        setLightEndVoltage.text.toString().toInt() * 100)
                } else {
                    Toast.makeText(applicationContext, "올바른 시간을 설정해 주십시오", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }


    }

    override fun onDestroy() {
        manager.removeEvent(Constants.SOCKET_START_Light)
        manager.removeEvent(Constants.SOCKET_END_Light)
        manager.removeEvent(Constants.SOCKET_START_Voltage)
        manager.removeEvent(Constants.SOCKET_END_Voltage)
        super.onDestroy()
    }
}