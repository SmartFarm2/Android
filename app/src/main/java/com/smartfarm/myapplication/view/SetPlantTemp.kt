package com.smartfarm.myapplication.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.smartfarm.myapplication.R
import com.smartfarm.myapplication.application.MyApp
import com.smartfarm.myapplication.databinding.ActivitySetPlantTempBinding
import com.smartfarm.myapplication.viewmodel.SetPlantTempViewModel
import com.smartfarm.myapplication.viewmodel.SetPlantViewModelFactory

class SetPlantTemp : AppCompatActivity() {

    private lateinit var binding : ActivitySetPlantTempBinding
    private lateinit var viewModel : SetPlantTempViewModel
    private lateinit var viewModelFactory : SetPlantViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(MyApp.pref.startDoor != "") {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        binding = DataBindingUtil.setContentView(this, R.layout.activity_set_plant_temp)

        viewModelFactory = SetPlantViewModelFactory(application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(SetPlantTempViewModel::class.java)
//현재값 유지 할지

        with(binding){
            lifecycleOwner = this@SetPlantTemp
            myViewModel = viewModel
        }

        with(viewModel) {
            observing()

            statusData.observe(this@SetPlantTemp){
                if(!statusData.value.isNullOrEmpty()){
                    Toast.makeText(this@SetPlantTemp, "값설정에 성공하였습니다.", Toast.LENGTH_SHORT).show()
                    MyApp.pref.openerSetting = it
                    startActivity(Intent(this@SetPlantTemp, SetNameActivity::class.java))
                    finish()
                }else{
                    Toast.makeText(this@SetPlantTemp, "관리자에게 문의하시오..", Toast.LENGTH_SHORT).show()
                }
            }

            toasts.observe(this@SetPlantTemp) {
                it.getContentIfNotHandled()?.let {
                    Toast.makeText(this@SetPlantTemp, it, Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    override fun onDestroy() {
        viewModel.deobserving()
        super.onDestroy()
    }
}