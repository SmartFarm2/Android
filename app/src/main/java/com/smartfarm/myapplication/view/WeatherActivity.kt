package com.smartfarm.myapplication.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.smartfarm.myapplication.R
import com.smartfarm.myapplication.databinding.ActivityWeatherBinding
import com.smartfarm.myapplication.viewmodel.WeatherActivityViewModel
import com.smartfarm.myapplication.viewmodel.WeatherActivityViewModelFactory

class WeatherActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWeatherBinding
    private lateinit var viewModel: WeatherActivityViewModel
    private lateinit var viewModelFactory: WeatherActivityViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_weather)

        viewModelFactory = WeatherActivityViewModelFactory(application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(WeatherActivityViewModel::class.java)

        with(binding){
            myViewModel = viewModel
            lifecycleOwner = this@WeatherActivity

            refreshView.setOnRefreshListener {
                viewModel.getWeather()
                refreshView.isRefreshing = false;
            }
        }

        with(viewModel){
            getWeather()
        }
    }
}