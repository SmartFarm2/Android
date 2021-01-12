package com.smartfarm.myapplication.bindings

import android.graphics.Typeface
import android.widget.TextView
import androidx.databinding.BindingAdapter

object TextViewBindings {
    @JvmStatic
    @BindingAdapter("android:isBold")
    fun isBold(view : TextView, isBold : Boolean) {
        if(isBold) view.setTypeface(null, Typeface.BOLD)
        else view.setTypeface(null, Typeface.NORMAL)
    }
}