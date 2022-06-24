package com.athkar.sa.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigator
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.athkar.sa.databinding.CounterAlthkerItemsBinding
import com.athkar.sa.db.entity.CounterAlthker

class CounterAlthkerAdapter(private val onClick:(name:String)->Unit):ListAdapter<CounterAlthker,CounterItemViewHolder>(CounterAlthkerDifUtil()) {
    class CounterAlthkerDifUtil():DiffUtil.ItemCallback<CounterAlthker>(){
        override fun areItemsTheSame(oldItem: CounterAlthker, newItem: CounterAlthker): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: CounterAlthker, newItem: CounterAlthker): Boolean {
           return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CounterItemViewHolder {
        return CounterItemViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: CounterItemViewHolder, position: Int) {
        val items = getItem(position)
        holder.bind(items,onClick)
    }
}

class CounterItemViewHolder(private val binding : CounterAlthkerItemsBinding):RecyclerView.ViewHolder(binding.root){

    fun bind(counterAlthker: CounterAlthker,onClick:(name:String)->Unit){
        binding.root.setOnClickListener{
            onClick(counterAlthker.name)
        }
        binding.nameAlthkr.text = counterAlthker.name
        binding.root.tag = counterAlthker
        binding.count.text = counterAlthker.count.toString()
    }

    companion object{
        fun from (parent:ViewGroup):CounterItemViewHolder{
            val inflater = LayoutInflater.from(parent.context)
            val binding = CounterAlthkerItemsBinding.inflate(inflater,parent,false)
            return CounterItemViewHolder(binding)
        }
    }
}