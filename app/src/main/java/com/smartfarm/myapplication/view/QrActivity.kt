package com.smartfarm.myapplication.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.smartfarm.myapplication.R
import com.smartfarm.myapplication.application.MyApp
import com.smartfarm.myapplication.databinding.ActivityQrBinding
import kotlinx.android.synthetic.main.activity_qr.*
import kotlinx.android.synthetic.main.activity_signup.*

class QrActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQrBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_qr)

        when{
            MyApp.pref.serverAddress != "DEFAULT_ADDRESS" -> {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }

        qr_scan_btn.setOnClickListener {
            startActivity(Intent(this, QrScanActivity::class.java))
            finish()
        }
    }
}