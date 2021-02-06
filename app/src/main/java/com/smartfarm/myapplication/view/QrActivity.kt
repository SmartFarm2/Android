package com.smartfarm.myapplication.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.smartfarm.myapplication.R
import com.smartfarm.myapplication.application.MyApp
import com.smartfarm.myapplication.data.Constants
import com.smartfarm.myapplication.databinding.ActivityQrBinding

class QrActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQrBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_MyApplication)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_qr)
        if(MyApp.pref.serverAddress != Constants.SERVER_ADDRESS) {
            startActivity(Intent(this,  SetPlantTemp::class.java))
            finish()
        }

        val intent = Intent(this, QrScanActivity::class.java)
        with(binding){
            lifecycleOwner = this@QrActivity

            qrScanBtn.setOnClickListener {
                startActivity(intent)
                finish()
            }
        }
    }
}