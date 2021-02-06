package com.smartfarm.myapplication.data

object Constants {
    const val channelID = "com.example.socketiobackground.channel1"
    const val notificationId = 123

    const val channelName = "SERVER SOCKET"
    const val channelDescription = "서버와 연결하기위한 서비스 입니다."

    const val SERVER_DATA_KEY = "SERVER_ADDRESS_KEY"
    const val PASSWORD_DATA_KEY = "PASSWORD_KEY"
    const val OPENER_DATA_KEY = "OPENER_EKY"
    const val NAME_DATA_KEY = "name"
    const val LAST_CHECK_NOTI_KEY = "noti"
    const val DOOR_SET_KEY = "DOOR_SET_KEY"
    const val START_DOOR_KEY = "START_DOOR_KEY"
    const val END_DOOR_KEY = "END_DOOR_KEY"
    const val START_LIGHT_KEY = "START_DOOR_KEY"
    const val END_LIGHT_KEY = "END_DOOR_KEY"
    const val INIT = "init"
    const val AUTO = "setAuto"

    const val SERVER_ADDRESS = "DEFAULT_ADDRESS"
    const val SOCKET_TEMP = "temp"
    const val SOCKET_TEMP_INSIDE = "tempInside"
    const val SOCKET_HUMI_INSIDE = "humiInside"
    const val SOCKET_HUMI = "humi"
    const val SOCKET_CYCLE = "cycle"
    const val SOCKET_SET_TEMP = "setTemp"
    const val SOCKET_CYCLE_CHANGE = "changeCycle"
    const val SOCKET_DOOR = "door"
    const val SOCKET_START_DOOR = "onTime"
    const val SOCKET_END_DOOR = "offTime"
    const val SOCKET_START_Light = "onTime"
    const val SOCKET_END_Light = "offTime"
    const val SOCKET_CCTV = ":8080/hls/stream.m3u8"
    const val CCTV = "cctv"


    const val SOCKET_PASSWORD = "password"

    const val CONNECTED_KEY = "CONNECTED"
    const val DISCONNECTED_KEY = "DISCONNECTED"
    const val CONNECTING_KEY = "CONNECTING"

    const val WEATHER_URL = "/weather"

    const val NAME_VERIFY_KEY = "SUCCESS"
}