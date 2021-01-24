package com.smartfarm.myapplication.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
            startActivity(Intent(this, SetDoorActivity::class.java))
            finish()
        }

    }
}