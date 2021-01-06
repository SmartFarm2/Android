package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.myapplication.adapter.SpecialAdapter
import com.example.myapplication.data.FarmData
import com.example.myapplication.data.FarmInfoData
import com.example.myapplication.data.SpecialData
import com.example.myapplication.data.SpecialList
import com.example.myapplication.databinding.ActivityRecyclerBinding

class RecyclerActivity : AppCompatActivity() {
    lateinit var binding: ActivityRecyclerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recycler)
        binding.farmInfo = FarmData("adsf")
        binding.items = SpecialList(listOf(SpecialData(false, "현재 특이사항이 없습니다")))
        binding.info = FarmInfoData(
            temp = 20,
            humi = 20,
            message = 0,
            weather = "맑음",
            light = true,
            pump = false,
            fan = false,
            door = true
        )
        binding.recycler.adapter = SpecialAdapter()
    }
}