package com.smartfarm.myapplication.bindings

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.smartfarm.myapplication.adapter.SpecialAdapter
import com.smartfarm.myapplication.data.SpecialData

object RecylcerBindings {
    @JvmStatic
    @BindingAdapter("items")
    fun bindItems(recyclerView: RecyclerView, item : List<SpecialData>?) {
        recyclerView.adapter?.run {
            if(this is SpecialAdapter) {
                item?: return
                this.updateItem(item)
                this.notifyDataSetChanged()
            }
        }
    }
}