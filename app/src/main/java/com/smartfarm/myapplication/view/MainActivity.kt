package com.smartfarm.myapplication.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.smartfarm.myapplication.R
import com.smartfarm.myapplication.adapter.SpecialAdapter
import com.smartfarm.myapplication.application.MyApp
import com.smartfarm.myapplication.data.*
import com.smartfarm.myapplication.databinding.ActivityMainBinding
import com.smartfarm.myapplication.room.DataBase
import com.smartfarm.myapplication.viewmodel.MainActivityViewModel
import com.smartfarm.myapplication.viewmodel.MainActivityViewModelFactory
import java.util.*

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

            infoBox.notiBox.setOnClickListener {
                MyApp.pref.lastCheckNotiTime = Calendar.getInstance().timeInMillis
                startActivity(Intent(this@MainActivity, NotiBoxActivity::class.java))
            }



            weatherBox.infoBox.setOnClickListener {
                startActivity(Intent(this@MainActivity, WeatherActivity::class.java))
            }
        }

        with(viewModel) {
            observing()
            toasts.observe(this@MainActivity) {
                it.getContentIfNotHandled()?.let {
                    Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.infoBox.info = FarmInfoData(DataBase.getInstance(this@MainActivity)!!.dao().getMessageCount(MyApp.pref.lastCheckNotiTime).toInt())
    }

    override fun onDestroy() {
        viewModel.deobserving()
        super.onDestroy()
    }
}