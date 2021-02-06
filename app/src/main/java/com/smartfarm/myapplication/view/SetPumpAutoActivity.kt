package com.smartfarm.myapplication.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.smartfarm.myapplication.R
import com.smartfarm.myapplication.application.MyApp
import com.smartfarm.myapplication.data.Constants
import com.smartfarm.myapplication.databinding.ActivitySetPumpAutoBinding
import com.smartfarm.myapplication.network.SocketManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SetPumpAutoActivity : AppCompatActivity() {
    
    private lateinit var binding : ActivitySetPumpAutoBinding
    private lateinit var manager : SocketManager
    private var check : Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_set_pump_auto)
        manager = SocketManager.getInstance(application)

        manager.addEvent(Constants.SOCKET_PUMP_AUTO){
            CoroutineScope(Dispatchers.Main).launch {
                if(it[0] == check) {
                    MyApp.pref.pump = "true"
                    Toast.makeText(this@SetPumpAutoActivity, "설정이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@SetPumpAutoActivity, MainActivity::class.java))
                    ActivityCompat.finishAffinity(this@SetPumpAutoActivity)
                }else{
                    Toast.makeText(this@SetPumpAutoActivity, "설정에 실패하였습니다..", Toast.LENGTH_SHORT).show()
                }
            }
        }

        with(binding)
        {
            autoPump.setOnClickListener {
                autoPump.background = ContextCompat.getDrawable(this@SetPumpAutoActivity, R.drawable.door_click_background)
                manualPump.background = ContextCompat.getDrawable(this@SetPumpAutoActivity, R.drawable.door_background)
                check = true
            }

            manualPump.setOnClickListener {
                manualPump.background = ContextCompat.getDrawable(this@SetPumpAutoActivity, R.drawable.door_click_background)
                autoPump.background = ContextCompat.getDrawable(this@SetPumpAutoActivity, R.drawable.door_background)
                check = false
            }

            setPumpAutoNext.setOnClickListener {
                if(check == null) {
                    Toast.makeText(this@SetPumpAutoActivity, "자동과 수동중 선택해주세요.", Toast.LENGTH_SHORT).show()
                }else {
                    manager.emit(Constants.SOCKET_PUMP_AUTO, check!!)
                }
            }
        }

    }
}