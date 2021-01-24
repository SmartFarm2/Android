package com.smartfarm.myapplication.room

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao

@Dao
interface Dao {

    @Query("select COUNT(*) from noti where time > :currentTime")
    fun getMessageCount(currentTime : Long) : Long

    @Query("SELECT * FROM noti order by time DESC")
    fun getAll() : List<NotiDataBase>

    @Insert
    fun insert(notiDataBase: NotiDataBase)

    @Update
    fun update(notiDataBase: NotiDataBase)
}