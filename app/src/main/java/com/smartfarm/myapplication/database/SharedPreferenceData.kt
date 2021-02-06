package com.smartfarm.myapplication.database

import android.content.Context
import android.content.SharedPreferences
import com.smartfarm.myapplication.data.Constants

class SharedPreferenceData(context: Context) {
    private val PREF_FILE_NAME = "ServerInfo"

    val pref: SharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, 0)

    var serverAddress: String
        get() = pref.getString(Constants.SERVER_DATA_KEY, Constants.SERVER_ADDRESS)!!
        set(value) = pref.edit().putString(Constants.SERVER_DATA_KEY, value).apply()
    var password: String
        get() = pref.getString(Constants.PASSWORD_DATA_KEY, "")!!
        set(value) = pref.edit().putString(Constants.PASSWORD_DATA_KEY, value).apply()
    var openerSetting: String
        get() = pref.getString(Constants.OPENER_DATA_KEY, "")!!
        set(value) = pref.edit().putString(Constants.OPENER_DATA_KEY, value).apply()
    var openerSetting2: String
        get() = pref.getString(Constants.OPENER_DATA_KEY2, "")!!
        set(value) = pref.edit().putString(Constants.OPENER_DATA_KEY2, value).apply()
    var name: String
        get() = pref.getString(Constants.NAME_DATA_KEY, "")!!
        set(value) = pref.edit().putString(Constants.NAME_DATA_KEY, value).apply()
    var lastCheckNotiTime: Long
        get() = pref.getLong(Constants.LAST_CHECK_NOTI_KEY, 0)!!
        set(value) = pref.edit().putLong(Constants.LAST_CHECK_NOTI_KEY, value).apply()
    var doorSetting: Boolean
        get() = pref.getBoolean(Constants.DOOR_SET_KEY, true)!!
        set(value) = pref.edit().putBoolean(Constants.DOOR_SET_KEY, value).apply()
    var clientTemp: Int
        get() = pref.getInt(Constants.DOOR_SET_KEY, 0)!!
        set(value) = pref.edit().putInt(Constants.DOOR_SET_KEY, value).apply()
    var startDoor: String
        get() = pref.getString(Constants.START_DOOR_KEY, "")!!
        set(value) = pref.edit().putString(Constants.START_DOOR_KEY, value).apply()
    var endDoor: String
        get() = pref.getString(Constants.END_DOOR_KEY, "")!!
        set(value) = pref.edit().putString(Constants.END_DOOR_KEY, value).apply()
    var startLight: String
        get() = pref.getString(Constants.START_LIGHT_KEY, "")!!
        set(value) = pref.edit().putString(Constants.START_DOOR_KEY, value).apply()
    var endLight: String
        get() = pref.getString(Constants.END_LIGHT_KEY, "")!!
        set(value) = pref.edit().putString(Constants.END_DOOR_KEY, value).apply()
}