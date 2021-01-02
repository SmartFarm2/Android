package com.example.myapplication

import android.content.Context
import android.content.SharedPreferences
import com.example.myapplication.Constants

class SharedPreferenceData(context: Context) {
    private val PREF_FILE_NAME = "ServerInfo"

    val pref: SharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, 0)

    var serverAddress: String
        get() = pref.getString(Constants.SERVER_DATA_KEY, Constants.SERVER_ADDRESS)!!
        set(value) = pref.edit().putString(Constants.SERVER_DATA_KEY, value).apply()
}