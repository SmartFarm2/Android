package com.smartfarm.myapplication.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.smartfarm.myapplication.R
import com.smartfarm.myapplication.adapter.NotiBoxAdapter
import com.smartfarm.myapplication.databinding.ActivityNotiBoxBinding
import com.smartfarm.myapplication.room.DataBase
import com.smartfarm.myapplication.room.NotiDataBase

class NotiBoxActivity : AppCompatActivity() {

    private lateinit var binding : ActivityNotiBoxBinding
    private lateinit var adapter : NotiBoxAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_noti_box)
        initRecyclerView()
    }

    private fun initRecyclerView(){
        binding.notiboxRecyclerview.layoutManager = LinearLayoutManager(this)
        adapter = NotiBoxAdapter()
        binding.notiboxRecyclerview.adapter = adapter
        adapter.setList(DataBase.getInstance(this)!!.dao().getAll())
    }
}