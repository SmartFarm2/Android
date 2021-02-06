package com.smartfarm.myapplication.view

import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.SyncStateContract
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.smartfarm.myapplication.R
import com.smartfarm.myapplication.application.MyApp
import com.smartfarm.myapplication.data.Constants
import com.smartfarm.myapplication.databinding.ActivitySetPumpBinding
import com.smartfarm.myapplication.network.SocketManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class SetPumpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySetPumpBinding
    private lateinit var manager: SocketManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        manager = SocketManager.getInstance(application)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_set_pump)

        manager.addEvent(Constants.SOCKET_START_PUMP){
            CoroutineScope(Dispatchers.Main).launch {
                if (it[0] == binding.setPumpStartTime.text.toString().toInt()) {
                    Toast.makeText(this@SetPumpActivity, "설정이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@SetPumpActivity, SetPumpAutoActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this@SetPumpActivity, "설정에 실패하였습니다..", Toast.LENGTH_SHORT).show()
                }
            }
        }

        with(binding) {
            setPumpStartTimeButton.setOnClickListener {
                getTime { setPumpStartTime.text = it }
            }

            setPumpEndTimeButton.setOnClickListener {
                getTime { setPumpEndTime.text = it }
            }

            setPumpNext.setOnClickListener {
                if(!setPumpStartTime.text.isNullOrEmpty() && !setPumpEndTime.text.isNullOrEmpty()) {
                manager.emit(Constants.SOCKET_START_PUMP, setPumpStartTime.text.toString().toInt())
                manager.emit(Constants.SOCKET_END_PUMP, setPumpEndTime.text.toString().toInt())
                }else{
                    Toast.makeText(this@SetPumpActivity, "값을 모두 설정해주세요.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun getTime(callback : (String) -> Unit) {
        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            callback(SimpleDateFormat("HHmm").format(cal.time))
        }
        TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
    }

    override fun onDestroy() {
        manager.removeEvent(Constants.SOCKET_START_PUMP)
        manager.removeEvent(Constants.SOCKET_END_PUMP)
        super.onDestroy()
    }
}