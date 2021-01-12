package com.smartfarm.myapplication.data

data class FarmData(
    val _name : String
) {
    val name : String get()=_name + "의 농장"
}
