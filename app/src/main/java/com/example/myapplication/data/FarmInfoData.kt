package com.example.myapplication.data

data class FarmInfoData(
    var temp: Int,
    var humi: Int,
    var message: Int,
    var weather: String,
    var light: Boolean,
    var pump: Boolean,
    var fan: Boolean,
    var door: Boolean
)
