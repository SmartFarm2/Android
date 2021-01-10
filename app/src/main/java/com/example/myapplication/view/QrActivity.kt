package com.example.myapplication.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityQrBinding
import kotlinx.android.synthetic.main.activity_qr.*

class QrActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQrBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_qr)

        qr_scan_btn.setOnClickListener {
            startActivity(Intent(this, QrScanActivity::class.java))
            finish()
        }
    }
}