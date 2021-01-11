package com.example.myapplication.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.R
import com.example.myapplication.adapter.SpecialAdapter
import com.example.myapplication.data.*
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.viewmodel.MainActivityViewModel
import com.example.myapplication.viewmodel.MainActivityViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var viewModelFactory: MainActivityViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.farmInfo = FarmData("양준혁")
        binding.items = SpecialList(listOf(SpecialData(false, "현재 특이사항이 없습니다")))

        viewModelFactory = MainActivityViewModelFactory(20, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainActivityViewModel::class.java)

        with(binding) {
            myViewModel = viewModel
            lifecycleOwner = this@MainActivity
            recycler.adapter = SpecialAdapter()
            cycle.infoBox.setOnClickListener {
                viewModel.setCycle()
            }

            door.infoBox.setOnClickListener {
                viewModel.setDoor()
            }

            cctvView.infoBox.setOnClickListener {
                startActivity(Intent(this@MainActivity, CCTVActivity::class.java))
            }

            refreshView.setOnRefreshListener {
                viewModel.getWeather()
                refreshView.isRefreshing = false;
            }
        }

        with(viewModel) {
            getWeather()
            observing()
            toasts.observe(this@MainActivity) {
                it.getContentIfNotHandled()?.let {
                    Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroy() {
        viewModel.deobserving()
        super.onDestroy()
    }
}