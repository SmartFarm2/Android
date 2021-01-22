package com.smartfarm.myapplication.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.smartfarm.myapplication.R
import com.smartfarm.myapplication.application.MyApp
import com.smartfarm.myapplication.databinding.ActivitySetDoorBinding
import com.smartfarm.myapplication.network.SocketManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class SetDoorActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySetDoorBinding
    private lateinit var manager: SocketManager
    private var check : Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_set_door)
        manager = SocketManager.getInstance(application)

        manager.addEvent("setAuto"){
            CoroutineScope(Dispatchers.Main).launch {
                if(it[0] == check) {
                    Toast.makeText(this@SetDoorActivity, "설정이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@SetDoorActivity, MainActivity::class.java))
                }else{
                    Toast.makeText(this@SetDoorActivity, "설정에 실패하였습니다..", Toast.LENGTH_SHORT).show()
                }
            }
        }

        with(binding)
        {
            autoDoor.setOnClickListener {
                autoDoor.background = ContextCompat.getDrawable(this@SetDoorActivity, R.drawable.door_click_background)
                manualDoor.background = ContextCompat.getDrawable(this@SetDoorActivity, R.drawable.door_background)
                check = true
            }

            manualDoor.setOnClickListener {
                manualDoor.background = ContextCompat.getDrawable(this@SetDoorActivity, R.drawable.door_click_background)
                autoDoor.background = ContextCompat.getDrawable(this@SetDoorActivity, R.drawable.door_background)
                check = false
            }

            setDoorNext.setOnClickListener {
                if(check == null) {
                    Toast.makeText(this@SetDoorActivity, "자동과 수동중 선택해주세요.", Toast.LENGTH_SHORT).show()
                }else {
                    manager.emit("setAuto", check!!)
                }
            }
        }

    }
}