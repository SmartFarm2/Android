package com.smartfarm.myapplication.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.smartfarm.myapplication.R
import com.smartfarm.myapplication.application.MyApp
import com.smartfarm.myapplication.data.Constants
import com.smartfarm.myapplication.databinding.ActivitySetTimeBinding
import com.smartfarm.myapplication.network.SocketManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SetTimeActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySetTimeBinding
    private lateinit var manager: SocketManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_set_time)
        manager = SocketManager.getInstance(application)

        Log.d("TAG", "생겨남")

        var isFirst = intent.getStringExtra("door")

        manager.addEvent(Constants.SOCKET_START_DOOR){
            CoroutineScope(Dispatchers.Main).launch {
                if(it[0] == binding.setTimeStartHour.text.toString().toInt() * 100) {
                    MyApp.pref.startDoor = (binding.setTimeStartHour.text.toString().toInt() * 100).toString()
                    MyApp.pref.endDoor = (binding.setTimeEndHour.text.toString().toInt() * 100).toString()
                    Toast.makeText(this@SetTimeActivity, "설정이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@SetTimeActivity, SetDoorActivity::class.java).putExtra("door", isFirst))
                    finish()
                    Log.d("TAG", "사라짐")
                }else{
                    Toast.makeText(this@SetTimeActivity, "설정에 실패하였습니다..", Toast.LENGTH_SHORT).show()
                }
            }
        }

        with(binding) {
            setTimeNext.setOnClickListener {
                if(!setTimeStartHour.text.isNullOrEmpty() && !setTimeEndHour.text.isNullOrEmpty() && setTimeStartHour.text.toString().toInt() < 24 && setTimeEndHour.text.toString().toInt() < 24) {
                    manager.emit(Constants.SOCKET_START_DOOR, setTimeStartHour.text.toString().toInt() * 100)
                    manager.emit(Constants.SOCKET_END_DOOR, setTimeEndHour.text.toString().toInt() * 100)
                }else{
                    Toast.makeText(applicationContext, "올바른 시간을 설정해 주십시오", Toast.LENGTH_SHORT).show()
                }
            }
        }


    }

    override fun onDestroy() {
        manager.removeEvent(Constants.SOCKET_START_DOOR)
        manager.removeEvent(Constants.SOCKET_END_DOOR)
        super.onDestroy()
    }
}