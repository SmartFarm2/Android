package com.example.myapplication

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.example.myapplication.databinding.ActivityQrScanBinding
import kotlinx.android.synthetic.main.activity_qr_scan.*

private const val CAMERA_REQUEST_CODE = 101

class QrScanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQrScanBinding
    private lateinit var viewModel: QrScanActivityViewModel
    private lateinit var viewModelFactory : QrScanActivityViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_qr_scan)

        viewModelFactory = QrScanActivityViewModelFactory(application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(QrScanActivityViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setupPermissions()
        codeScanner()

        scanner_view.setOnClickListener {
           viewModel.codeScanner.startPreview()
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
                    Toast.makeText(this, "허락해줘", Toast.LENGTH_SHORT).show()
                }
                else{
                    //성공
                }
            }
        }
    }
}