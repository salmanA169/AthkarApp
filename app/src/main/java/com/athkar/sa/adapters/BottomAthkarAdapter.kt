package com.athkar.sa.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.athkar.sa.databinding.BottomAthkarItemBinding

class BottomAthkarAdapter (private val click:(String)->Unit): ListAdapter<String, BottomAthkarViewHolder>(BottomAthkarDifUtil()) {
    private class BottomAthkarDifUtil : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomAthkarViewHolder {
        return BottomAthkarViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: BottomAthkarViewHolder, position: Int) {
        val items = getItem(position)
        holder.bind(items,click)
    }
}

class BottomAthkarViewHolder(private val binding: BottomAthkarItemBinding) :
    RecyclerView.ViewHolder(binding.root){

    companion object{
        fun from(parent:ViewGroup):BottomAthkarViewHolder{
            val inflater = LayoutInflater.from(parent.context)
            val binding = BottomAthkarItemBinding.inflate(inflater,parent,false)
            return BottomAthkarViewHolder(binding)
        }
    }
        fun bind(nameAlthker:String,click: (String) -> Unit){
            binding.nameAlther.text = nameAlthker
            binding.tvPosition.text = bindingAdapterPosition.plus(1).toString()
            binding.root.setOnClickListener {
                click(nameAlthker)
            }
        }
    }