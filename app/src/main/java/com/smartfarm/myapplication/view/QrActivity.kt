package com.smartfarm.myapplication.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.smartfarm.myapplication.R
import com.smartfarm.myapplication.application.MyApp
import com.smartfarm.myapplication.data.Constants
import com.smartfarm.myapplication.databinding.ActivityQrBinding
import kotlinx.android.synthetic.main.activity_qr.*

class QrActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQrBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_qr)
        if(MyApp.pref.serverAddress != Constants.SERVER_ADDRESS) {
            startActivity(Intent(this, SetPlantTemp::class.java))
            finish()
        }
        qr_scan_btn.setOnClickListener {
            startActivity(Intent(this, QrScanActivity::class.java))
            finish()
        }
    }
}