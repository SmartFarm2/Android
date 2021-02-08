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
import com.smartfarm.myapplication.databinding.ActivitySetVoltBinding
import com.smartfarm.myapplication.network.SocketManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SetVoltActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySetVoltBinding
    private lateinit var manager: SocketManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_set_volt)
        manager = SocketManager.getInstance(application)

        Log.d("TAG", "생겨남")

        var isFirst = intent.getStringExtra("volt")

        manager.addEvent(Constants.SOCKET_START_VOLTAGE) {
            CoroutineScope(Dispatchers.Main).launch {
                Log.d("TAG", "성고23")
                if (it[0] == ((binding.setVoltStartVoltage.text.toString()
                        .toInt() * 100) + binding.setVoltStartVoltageMin.text.toString().toInt())
                ) {
                    MyApp.pref.startVoltage =
                        ((binding.setVoltStartVoltage.text.toString()
                            .toInt() * 100) + binding.setVoltStartVoltageMin.text.toString()
                            .toInt()).toString()
                    MyApp.pref.endVoltage =
                        ((binding.setVoltEndVoltage.text.toString()
                            .toInt() * 100) + binding.setVoltEndVoltageMin.text.toString()
                            .toInt()).toString()
                    Toast.makeText(this@SetVoltActivity, "설정이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                    startActivity(
                        Intent(
                            this@SetVoltActivity,
                            SetVoltageAutoActivity::class.java
                        ).putExtra("volt", isFirst)
                    )

                    finish()
                    Log.d("TAG", "사라짐")
                } else {
                    Toast.makeText(this@SetVoltActivity, "설정에 실패하였습니다..", Toast.LENGTH_SHORT)
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
                    && !setVoltStartVoltageMin.text.isNullOrEmpty()
                    && !setVoltEndVoltageMin.text.isNullOrEmpty()
                    && setVoltStartVoltageMin.text.toString().toInt() < 60
                    && setVoltEndVoltageMin.text.toString().toInt() < 60
                ) {
                    Log.d("TAG", "성고233")
                    manager.emit(
                        Constants.SOCKET_START_VOLTAGE, ((setVoltStartVoltage.text.toString()
                            .toInt() * 100) + setVoltStartVoltageMin.text.toString().toInt())
                    )
                    manager.emit(
                        Constants.SOCKET_END_VOLTAGE, ((setVoltEndVoltage.text.toString()
                            .toInt() * 100) + setVoltEndVoltageMin.text.toString().toInt())
                    )
                } else {
                    Toast.makeText(applicationContext, "올바른 시간을 설정해 주십시오", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }


    }

    override fun onDestroy() {
        manager.removeEvent(Constants.SOCKET_START_VOLTAGE)
        manager.removeEvent(Constants.SOCKET_END_VOLTAGE)
        super.onDestroy()
    }
}