package com.smartfarm.myapplication.data

import com.smartfarm.myapplication.application.MyApp

data class FarmData(
    val _name : String
) {
    val name : String get()= MyApp.pref.name + "의 농장"
}
