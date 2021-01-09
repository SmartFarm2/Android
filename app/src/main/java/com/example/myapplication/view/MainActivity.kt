package com.example.myapplication.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.R
import com.example.myapplication.adapter.SpecialAdapter
import com.example.myapplication.data.*
import com.example.myapplication.databinding.ActivityRecyclerBinding
import com.example.myapplication.viewmodel.MainActivityViewModel
import com.example.myapplication.viewmodel.MainActivityViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecyclerBinding
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var viewModelFactory: MainActivityViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recycler)
        binding.farmInfo = FarmData("양준혁")
        binding.items = SpecialList(listOf(SpecialData(false, "현재 특이사항이 없습니다")))

        viewModelFactory = MainActivityViewModelFactory(20, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainActivityViewModel::class.java)

        binding.myViewModel = viewModel
        binding.lifecycleOwner = this
        binding.recycler.adapter = SpecialAdapter()

        viewModel.getTemp(application)
        viewModel.getCycle(application)
        viewModel.getDoor(application)

        binding.cycle.infoBox.setOnClickListener {
            viewModel.setCycle(application)
        }

        binding.door.infoBox.setOnClickListener {
            viewModel.setDoor(application)
        }

        binding.cctvView.infoBox.setOnClickListener {
            startActivity(Intent(this, CCTVActivity::class.java))
        }
    }
}