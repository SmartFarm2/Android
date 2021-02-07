package com.smartfarm.myapplication.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.smartfarm.myapplication.R
import com.smartfarm.myapplication.application.MyApp
import com.smartfarm.myapplication.databinding.ActivitySettingBinding
import com.smartfarm.myapplication.room.DataBase
import com.smartfarm.myapplication.room.NotiDataBase

class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@SettingActivity, R.layout.activity_setting)

        binding.doorTimeSetting.setOnClickListener {
            startActivity(Intent(this, SetTimeActivity::class.java).putExtra("door", "door"))
            finish()
        }

        binding.pumpTimeSetting.setOnClickListener {
            startActivity(Intent(this, SetPumpActivity::class.java))
            finish()
        }

        binding.voltageTimeSetting.setOnClickListener {
            startActivity(Intent(this, SetVoltActivity::class.java).putExtra("volt", "volt"))
            finish()
        }

        binding.voltage220TimeSetting.setOnClickListener {
            startActivity(Intent(this, Set220Activity::class.java).putExtra("volt220", "volt220"))
            finish()
        }

    }

}