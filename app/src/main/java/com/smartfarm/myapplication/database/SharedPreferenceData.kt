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
    var name: String
        get() = pref.getString(Constants.NAME_DATA_KEY, "")!!
        set(value) = pref.edit().putString(Constants.NAME_DATA_KEY, value).apply()
}