package com.example.myapplication.data

data class SpecialData(val isError : Boolean, val message : String)
data class SpecialList(val list : List<SpecialData>)