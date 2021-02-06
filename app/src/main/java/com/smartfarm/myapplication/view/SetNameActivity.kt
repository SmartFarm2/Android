package com.smartfarm.myapplication.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.smartfarm.myapplication.R
import com.smartfarm.myapplication.application.MyApp
import com.smartfarm.myapplication.data.Constants
import com.smartfarm.myapplication.databinding.ActivitySetNameBinding
import com.smartfarm.myapplication.viewmodel.SetNameViewModel

class SetNameActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySetNameBinding
    private lateinit var viewModel : SetNameViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = DataBindingUtil.setContentView(this, R.layout.activity_set_name)
        viewModel = ViewModelProvider(this).get(SetNameViewModel::class.java)

        with(binding){
            lifecycleOwner = this@SetNameActivity
            myViewModel = viewModel
        }

        with(viewModel){
            toasts.observe(this@SetNameActivity) {
                it.getContentIfNotHandled()?.let {
                    if(it == Constants.NAME_VERIFY_KEY) {
                        startActivity(Intent(this@SetNameActivity, SetTimeActivity::class.java))
                        finish()
                    }
                    else {
                        Toast.makeText(this@SetNameActivity, it, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}