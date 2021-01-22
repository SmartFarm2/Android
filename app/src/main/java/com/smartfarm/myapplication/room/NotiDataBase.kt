package com.smartfarm.myapplication.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "noti")
data class NotiDataBase(
    @PrimaryKey(autoGenerate = true) var id: Long,
    @ColumnInfo(name = "message") var message: String,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "time") var time: Long
)