package com.smartfarm.myapplication.view

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.smartfarm.myapplication.R
import com.smartfarm.myapplication.application.MyApp
import com.smartfarm.myapplication.databinding.ActivityQrScanBinding
import kotlinx.android.synthetic.main.activity_qr_scan.*

private const val CAMERA_REQUEST_CODE = 101

class QrScanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQrScanBinding

    private lateinit var codeScanner : CodeScanner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_qr_scan)

        setupPermissions()
        codeScanner()
    }

    private fun codeScanner() {
        codeScanner = CodeScanner(this, binding.scan)

        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS

            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.CONTINUOUS
            isAutoFocusEnabled = true
            isFlashEnabled = false

            decodeCallback = DecodeCallback {
                //서버주소 변경
                MyApp.pref.serverAddress = it.text
                startActivity(Intent(this@QrScanActivity, MainActivity::class.java))

                finish()
            }

            errorCallback = ErrorCallback {
                runOnUiThread {
                    Log.e("Main", "Camera initialzation error : ${it.message}")
                }
            }
        }

        scan.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(this,
            android.Manifest.permission.CAMERA)

        if(permission != PackageManager.PERMISSION_GRANTED){
            makeRequest()
        }
    }

    private fun makeRequest(){
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "카메라 권한을 허용해 주십시오.", Toast.LENGTH_SHORT).show()
                }
                else{
                    //성공
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }
}