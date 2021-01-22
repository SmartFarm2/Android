package com.smartfarm.myapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.smartfarm.myapplication.R
import com.smartfarm.myapplication.databinding.LayoutNotiBoxBinding
import com.smartfarm.myapplication.room.NotiDataBase
import java.util.*
import kotlin.collections.ArrayList

class NotiBoxAdapter : RecyclerView.Adapter<NotiBoxAdapter.NotiBoxViewHolder>() {

    private val notiList = ArrayList<NotiDataBase>()

    fun setList(notis:List<NotiDataBase>){
        notiList.clear()
        notiList.addAll(notis)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotiBoxViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding : LayoutNotiBoxBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.layout_noti_box,
            parent,
            false
        )
        return NotiBoxViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return notiList.size
    }

    override fun onBindViewHolder(holder: NotiBoxViewHolder, position: Int) {
        holder.bind(notiList[position])
    }

    inner class NotiBoxViewHolder(private var binding: LayoutNotiBoxBinding) :
        RecyclerView.ViewHolder(binding.root){
        fun bind(notiDataBase: NotiDataBase){
            binding.content.text = notiDataBase.message
            binding.time.text = ((((Calendar.getInstance().timeInMillis - notiDataBase.time).toInt()) / 1000).toString()) + "초전"
            binding.title.text = notiDataBase.title
        }
    }
}