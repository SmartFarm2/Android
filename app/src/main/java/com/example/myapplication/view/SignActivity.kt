package com.example.myapplication.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.myapplication.application.MyApp
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivitySignupBinding
import kotlinx.android.synthetic.main.activity_signup.*

class SignActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup)

        nextButton.setOnClickListener {
            when {
//                setPassword.text.isNotEmpty() and (setPassword.text.length > 3) /*and (MyApp.pref.serverAddress != "")*/ -> {
//                    startActivity(Intent(this, MainActivity::class.java))
//                    finish()
//                }
                setPassword.text.isNotEmpty() and (setPassword.text.length > 3) /*and (MyApp.pref.serverAddress == "")*/ -> {
                    startActivity(Intent(this, QrActivity::class.java))
                    finish()
                }
                else -> {
                    Toast.makeText(this, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}