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
import com.smartfarm.myapplication.databinding.ActivitySet220Binding
import com.smartfarm.myapplication.databinding.ActivitySetVoltBinding
import com.smartfarm.myapplication.network.SocketManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Set220Activity : AppCompatActivity() {
    private lateinit var binding: ActivitySet220Binding
    private lateinit var manager: SocketManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_set_220)
        manager = SocketManager.getInstance(application)

        Log.d("TAG", "생겨남")

        var isFirst = intent.getStringExtra("volt220")

        manager.addEvent(Constants.SOCKET_START_220VOLTAGE) {
            CoroutineScope(Dispatchers.Main).launch {
                Log.d("TAG", "성고23")

                Log.d("TAG", "${it[0]}")
                if (it[0] == binding.setVoltStartVoltage.text.toString().toInt() * 100) {
                    MyApp.pref.start220Voltage =
                        (binding.setVoltStartVoltage.text.toString().toInt() * 100).toString()
                    MyApp.pref.end220Voltage =
                        (binding.setVoltEndVoltage.text.toString().toInt() * 100).toString()
                    Toast.makeText(this@Set220Activity, "설정이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@Set220Activity, Set220AutoActivity::class.java).putExtra("volt220", isFirst))

                    finish()
                    Log.d("TAG", "사라짐")
                } else {
                    Toast.makeText(this@Set220Activity, "설정에 실패하였습니다..", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        with(binding) {
            setVoltNext.setOnClickListener {
                if (!setVoltStartVoltage.text.isNullOrEmpty()
                    && !setVoltEndVoltage.text.isNullOrEmpty()
                    && setVoltStartVoltage.text.toString().toInt() < 24
                    && setVoltEndVoltage.text.toString().toInt() < 24
                ) {
                    Log.d("TAG", "성고233")
                    manager.emit(Constants.SOCKET_START_220VOLTAGE,
                        setVoltStartVoltage.text.toString().toInt() * 100)
                    manager.emit(Constants.SOCKET_END_220VOLTAGE,
                        setVoltEndVoltage.text.toString().toInt() * 100)
                } else {
                    Toast.makeText(applicationContext, "올바른 시간을 설정해 주십시오", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }


    }

    override fun onDestroy() {
        manager.removeEvent(Constants.SOCKET_START_220VOLTAGE)
        manager.removeEvent(Constants.SOCKET_END_220VOLTAGE)
        super.onDestroy()
    }
}