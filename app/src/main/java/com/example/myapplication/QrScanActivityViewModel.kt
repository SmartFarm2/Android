package com.example.myapplication

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import kotlinx.android.synthetic.main.activity_qr_scan.*

class QrScanActivityViewModel(private val application: Application) : ViewModel() {

    lateinit var codeScanner : CodeScanner

    private fun codeScanner() {
        codeScanner = CodeScanner(application, scanner_view)

        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS

            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.CONTINUOUS
            isAutoFocusEnabled = true
            isFlashEnabled = false

            decodeCallback = DecodeCallback {
                //서버주소 변경
                Toast.makeText(application, "QR인식에 성공하였습니다.", Toast.LENGTH_SHORT).show()
                    //finish()
            }

            errorCallback = ErrorCallback {
                Log.e("Main", "Camera initialzation error : ${it.message}")
            }
        }


    }


}