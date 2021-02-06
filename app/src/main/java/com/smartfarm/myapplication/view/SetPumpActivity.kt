package com.smartfarm.myapplication.view

import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.smartfarm.myapplication.R
import com.smartfarm.myapplication.databinding.ActivitySetPumpBinding
import java.text.SimpleDateFormat
import java.util.*

class SetPumpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySetPumpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_set_pump)

        with(binding) {
            setPumpStartTimeButton.setOnClickListener {
                getTime { setPumpStartTime.text = it }
            }

            setPumpEndTimeButton.setOnClickListener {
                getTime { setPumpEndTime.text = it }
            }
        }
    }

    private fun getTime(callback : (String) -> Unit) {
        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            callback(SimpleDateFormat("HH:mm").format(cal.time))
        }
        TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
    }
}