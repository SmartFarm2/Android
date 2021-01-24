package com.smartfarm.myapplication.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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

    override fun onPause() {
        super.onPause()
        manager.removeEvent(Constants.SOCKET_SET_TEMP)
        manager.removeEvent(Constants.AUTO)
    }
    override fun onResume() {
        super.onResume()
        val sweetAlertDialog =
            SweetAlertDialog(this@MainActivity, SweetAlertDialog.WARNING_TYPE)

        binding.infoBox.info = FarmInfoData(DataBase.getInstance(this@MainActivity)!!.dao()
            .getMessageCount(MyApp.pref.lastCheckNotiTime).toInt())

        manager.emit(Constants.INIT, 0)
        manager.addEvent(Constants.AUTO) {
            CoroutineScope(Dispatchers.IO).launch {
                Log.d("TAG", "하이")
                MyApp.pref.doorSetting = it[0] as Boolean
                Log.d("TAG", "하이2")
            }
        }
        manager.addEvent(Constants.SOCKET_SET_TEMP) {data ->
            Log.d("TAG", "하이3")
            CoroutineScope(Dispatchers.IO).launch {
                Log.d("TAG", "하이4")
                if (MyApp.pref.openerSetting.toInt() == (data[0] as String).toInt() || MyApp.pref.openerSetting.isEmpty()) {
                    Log.d("TAG", "하이5")
                    MyApp.pref.openerSetting = data[0] as String
                    Log.d("TAG", "하이6")
                } else {
                    runOnUiThread {
                        sweetAlertDialog
                            .setTitleText("개폐기 온도 중복")
                            .setContentText("기기에 이미 개폐기 온도 값이 설정이 되어있습니다. 기기의 온도 값을 사용하시겠습니까?")
                            .setConfirmText("네")
                            .setCancelText("아니오")
                            .setConfirmClickListener {
                                MyApp.pref.openerSetting = data[0] as String
                                sweetAlertDialog.dismiss()
                            }
                            .setCancelClickListener {
                                manager.emit(Constants.SOCKET_SET_TEMP, MyApp.pref.openerSetting)
                            }
                            .show()
                    }
                }
                Log.d("TAG", "하이7")
            }

            Log.d("TAG", "하이8")
        }

    }

    override fun onDestroy() {
        viewModel.deobserving()
        super.onDestroy()
    }
}