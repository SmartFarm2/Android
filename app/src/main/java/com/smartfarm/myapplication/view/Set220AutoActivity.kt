package com.smartfarm.myapplication.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.smartfarm.myapplication.R
import com.smartfarm.myapplication.data.Constants
import com.smartfarm.myapplication.databinding.ActivitySet220AutoBinding
import com.smartfarm.myapplication.databinding.ActivitySetVoltBinding
import com.smartfarm.myapplication.databinding.ActivitySetVoltageAutoBinding
import com.smartfarm.myapplication.network.SocketManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Set220AutoActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySet220AutoBinding
    private lateinit var manager : SocketManager
    private var check : Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var isFirst = intent.getStringExtra("volt220")

        binding = DataBindingUtil.setContentView(this, R.layout.activity_set_220_auto)
        manager = SocketManager.getInstance(application)

        manager.addEvent(Constants.SOCKET_220VOLTAGE_AUTO){
            CoroutineScope(Dispatchers.Main).launch {
                if(it[0] == check) {
                    if(isFirst == "volt220") {
                        Toast.makeText(this@Set220AutoActivity, "설정이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@Set220AutoActivity, MainActivity::class.java))
                        ActivityCompat.finishAffinity(this@Set220AutoActivity)
                    }else{
                        Toast.makeText(this@Set220AutoActivity, "설정이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@Set220AutoActivity, SetPumpActivity::class.java))
                        finish()
                    }
                }else{
                    Toast.makeText(this@Set220AutoActivity, "설정에 실패하였습니다..", Toast.LENGTH_SHORT).show()
                }
            }
        }

        with(binding)
        {
            autoVolt.setOnClickListener {
                autoVolt.background = ContextCompat.getDrawable(this@Set220AutoActivity, R.drawable.door_click_background)
                manualVolt.background = ContextCompat.getDrawable(this@Set220AutoActivity, R.drawable.door_background)
                check = true
            }

            manualVolt.setOnClickListener {
                manualVolt.background = ContextCompat.getDrawable(this@Set220AutoActivity, R.drawable.door_click_background)
                autoVolt.background = ContextCompat.getDrawable(this@Set220AutoActivity, R.drawable.door_background)
                check = false
            }

            setVoltageAutoNext.setOnClickListener {
                if(check == null) {
                    Toast.makeText(this@Set220AutoActivity, "자동과 수동중 선택해주세요.", Toast.LENGTH_SHORT).show()
                }else {
                    manager.emit(Constants.SOCKET_220VOLTAGE_AUTO, check!!)
                }
            }
        }

    }


    override fun onDestroy() {
        manager.removeEvent(Constants.SOCKET_220VOLTAGE_AUTO)
        super.onDestroy()
    }
}