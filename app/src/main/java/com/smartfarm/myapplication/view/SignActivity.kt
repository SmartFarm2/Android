package com.smartfarm.myapplication.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.smartfarm.myapplication.R
import com.smartfarm.myapplication.SocketManager
import com.smartfarm.myapplication.application.MyApp
import com.smartfarm.myapplication.databinding.ActivitySignupBinding
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.Socket

class SignActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    lateinit var manager : SocketManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup)
        manager = SocketManager.getInstance(application)
        manager.addEvent("password") {
            CoroutineScope(Dispatchers.Main).launch {
                if(it[0] == true) {
                    startActivity(Intent(this@SignActivity, MainActivity::class.java))
                    finish()
                }else {
                    Toast.makeText(this@SignActivity, "비밀번호를 확인하세요", Toast.LENGTH_SHORT).show()
                }
            }
        }
        if(MyApp.pref.password != "") {
            manager.emit("password", MyApp.pref.password)
        }
        nextButton.setOnClickListener {
            when {
                setPassword.text.isNotEmpty() and (setPassword.text.length > 3) and (MyApp.pref.serverAddress != "DEFAULT_ADDRESS") -> {
                    MyApp.pref.password = setPassword.text.toString()
                    manager.emit("password", setPassword.text.toString())
                }
                else -> {
                    Toast.makeText(this, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroy() {
        manager.removeEvent("password")
        super.onDestroy()
    }
}