package com.example.myapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.SpecialData
import com.example.myapplication.databinding.LayoutSpecialCardBinding

class SpecialAdapter : RecyclerView.Adapter<SpecialAdapter.SpecialViewHolder>() {
    private val datas = ArrayList<SpecialData>()

    fun updateItem(list: List<SpecialData>) {
        datas.clear()
        datas.addAll(list)
    }

    inner class SpecialViewHolder(private val binding: LayoutSpecialCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: SpecialData) {
            binding.special = data
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecialViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: LayoutSpecialCardBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.layout_special_card, parent, false)
        return SpecialViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SpecialViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    override fun getItemCount(): Int = datas.size
}