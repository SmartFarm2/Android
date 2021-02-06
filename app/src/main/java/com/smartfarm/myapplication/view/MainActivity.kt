package com.smartfarm.myapplication.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import cn.pedant.SweetAlert.SweetAlertDialog
import com.smartfarm.myapplication.R
import com.smartfarm.myapplication.adapter.SpecialAdapter
import com.smartfarm.myapplication.application.MyApp
import com.smartfarm.myapplication.data.*
import com.smartfarm.myapplication.databinding.ActivityMainBinding
import com.smartfarm.myapplication.network.SocketManager
import com.smartfarm.myapplication.room.DataBase
import com.smartfarm.myapplication.viewmodel.MainActivityViewModel
import com.smartfarm.myapplication.viewmodel.MainActivityViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var viewModelFactory: MainActivityViewModelFactory
    private lateinit var manager: SocketManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.farmInfo = FarmData("양준혁")
        binding.items = SpecialList(listOf(SpecialData(false, "현재 특이사항이 없습니다")))

        viewModelFactory = MainActivityViewModelFactory(20, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainActivityViewModel::class.java)

        manager = SocketManager.getInstance(application)

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

            door2.infoBox.setOnClickListener {
                viewModel.setDoor2()
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

            setting.setOnClickListener {
                startActivity(Intent(this@MainActivity, SettingActivity::class.java))
            }

            voltage.infoBox.setOnClickListener {
                viewModel.setVoltage()
            }

            pump.infoBox.setOnClickListener {
                viewModel.setPump()
            }
        }

        with(viewModel) {
            observing()
            getWeather()
            toasts.observe(this@MainActivity) {
                it.getContentIfNotHandled()?.let {
                    Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT).show()
                }
            }

            when(weatherData.value){
                "맑음"->{
                    when(skyData.value){
                        "맑음"->{
                            binding.weatherBox.img = ContextCompat.getDrawable(this@MainActivity, R.drawable.ic_sun)
                        }

                        "구름조금"->{
                            binding.weatherBox.img = ContextCompat.getDrawable(this@MainActivity, R.drawable.ic_less_cloud)
                        }

                        "구름많음"->{
                            binding.weatherBox.img = ContextCompat.getDrawable(this@MainActivity, R.drawable.ic_cloud)
                        }

                        "흐림"->{
                           binding.weatherBox.img = ContextCompat.getDrawable(this@MainActivity, R.drawable.ic_black_cloud)
                        }
                    }
                }
                "비", "비/눈"->{
                    binding.weatherBox.img = ContextCompat.getDrawable(this@MainActivity, R.drawable.ic_rain2)
                }
                "눈"->{
                    binding.weatherBox.img = ContextCompat.getDrawable(this@MainActivity, R.drawable.ic_snow)
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        manager.removeEvent(Constants.SOCKET_SET_TEMP)
        manager.removeEvent(Constants.AUTO)
    }

    override fun onDestroy() {
        viewModel.deobserving()
        super.onDestroy()
    }
}