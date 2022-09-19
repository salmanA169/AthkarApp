package com.athkar.sa.adapters

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.athkar.sa.databinding.FavoriteItemBinding
import com.athkar.sa.db.entity.FavoriteAthkar
import com.athkar.sa.db.entity.toFavoriteTracker
import com.athkar.sa.models.AthkarCategory
import com.athkar.sa.models.FavoriteTracker
import com.athkar.sa.uitls.parseColor

class FavoriteAdapter(
    private val click: (athkarCategory: AthkarCategory, nameAlthker: String) -> Unit,
) : ListAdapter<FavoriteAthkar, FavoriteViewHolder>(FavoriteDifUtil()) {
    class FavoriteDifUtil : DiffUtil.ItemCallback<FavoriteAthkar>() {
        override fun areItemsTheSame(oldItem: FavoriteAthkar, newItem: FavoriteAthkar): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: FavoriteAthkar, newItem: FavoriteAthkar): Boolean {
            return oldItem == newItem
        }
    }

    var tracker: SelectionTracker<FavoriteTracker>? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        return FavoriteViewHolder.from(parent)
    }


    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val items = getItem(position)
        holder.bind(items, click, currentList)
        tracker?.let {
            holder.updateUI(it.isSelected(currentList[position].toFavoriteTracker()))
        }
    }

}

class FavoriteViewHolder(private val binding: FavoriteItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private var keyName :FavoriteTracker ? =null
    fun bind(
        favoriteAtkar: FavoriteAthkar,
        click: (athkarCategory: AthkarCategory, nameAlthker: String) -> Unit,
        currentList: List<FavoriteAthkar>
    ) {
        binding.colorAthkar.dividerColor = favoriteAtkar.athkarCategory.color.parseColor()
        binding.nameAlthkr.text = favoriteAtkar.nameAlthkr
        binding.categoryAlthkr.text = favoriteAtkar.athkarCategory.nameAthkar
        binding.root.setOnClickListener {
            click(favoriteAtkar.athkarCategory,favoriteAtkar.nameAlthkr)
        }


        keyName = favoriteAtkar.toFavoriteTracker()

    }

    fun updateUI(isSelected: Boolean) {
        binding.root.isChecked = isSelected
    }

    fun getItem(): ItemDetailsLookup.ItemDetails<FavoriteTracker> =
        object : ItemDetailsLookup.ItemDetails<FavoriteTracker>() {
            override fun getPosition(): Int {
                return bindingAdapterPosition
            }

            override fun getSelectionKey(): FavoriteTracker? {
                return keyName
            }
        }

    companion object {
        fun from(parent: ViewGroup): FavoriteViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = FavoriteItemBinding.inflate(inflater, parent, false)
            return FavoriteViewHolder(binding)
        }
    }

}