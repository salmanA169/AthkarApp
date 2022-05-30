package com.athkar.sa.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.athkar.sa.databinding.FavoriteItemBinding
import com.athkar.sa.db.entity.FavoriteAthkar
import com.athkar.sa.uitls.setColorAthkar

class FavoriteAdapter:ListAdapter<FavoriteAthkar,FavoriteViewHolder>(FavoriteDifUtil()) {
    class FavoriteDifUtil:DiffUtil.ItemCallback<FavoriteAthkar>(){
        override fun areItemsTheSame(oldItem: FavoriteAthkar, newItem: FavoriteAthkar): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: FavoriteAthkar, newItem: FavoriteAthkar): Boolean {
           return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        return FavoriteViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
       val items = getItem(position)
        holder.bind(items)
    }
}

class FavoriteViewHolder(private val binding:FavoriteItemBinding):RecyclerView.ViewHolder(binding.root){

    fun bind(favoriteAtkar :FavoriteAthkar){
        binding.colorAthkar.dividerColor = favoriteAtkar.athkarCategory.setColorAthkar()
        binding.nameAlthkr.text = favoriteAtkar.nameAlthkr
        binding.categoryAlthkr.text = favoriteAtkar.athkarCategory.nameAthkar
    }
    companion object{
        fun from (parent:ViewGroup):FavoriteViewHolder{
            val inflater = LayoutInflater.from(parent.context)
            val binding = FavoriteItemBinding.inflate(inflater,parent,false)
            return FavoriteViewHolder(binding)
        }
    }
}