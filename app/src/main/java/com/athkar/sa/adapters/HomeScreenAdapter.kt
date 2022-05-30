package com.athkar.sa.adapters

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.ColorUtils
import androidx.navigation.NavDirections
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.athkar.sa.databinding.AthkarContainerBinding
import com.athkar.sa.databinding.AthkarItemBinding
import com.athkar.sa.models.AthkarCategory
import com.athkar.sa.ui.homeScreen.HomeScreenFragmentDirections
import com.athkar.sa.ui.homeScreen.UiHomeScreens
import com.athkar.sa.uitls.setColorAthkar

interface HomeScreenEvents {
    fun onAthkerClick(athkarCategory: AthkarCategory)
    fun onContainerClick(destination:NavDirections)
}

const val CONTAINER_ATHKAR = 0
const val ATHKAR_UI = 1
class HomeScreenAdapter(private val onClick: HomeScreenEvents) :
    ListAdapter<UiHomeScreens, RecyclerView.ViewHolder>(HomeScreenDifUtil()) {
    private class HomeScreenDifUtil : DiffUtil.ItemCallback<UiHomeScreens>() {
        override fun areItemsTheSame(oldItem: UiHomeScreens, newItem: UiHomeScreens): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: UiHomeScreens, newItem: UiHomeScreens): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
       return when(viewType){
            CONTAINER_ATHKAR->{AthkarContainerViewHolder.from(parent)}
            ATHKAR_UI->{ HomeScreenViewHolder.from(parent)}
           else -> throw IllegalArgumentException("not found view type")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).run {
            if (this is UiHomeScreens.AthkarContainer){
                CONTAINER_ATHKAR
            }else{
                ATHKAR_UI
            }
        }
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val items = getItem(position)
        if (holder is HomeScreenViewHolder && items is UiHomeScreens.AthkarsUI){
                holder.bind(items.athkar, onClick)
        }else if (holder is AthkarContainerViewHolder){
            holder.bind(onClick)
        }
    }



}

class AthkarContainerViewHolder(private val binding:AthkarContainerBinding):RecyclerView.ViewHolder(binding.root){


    fun bind(onClick: HomeScreenEvents){

        binding.btnPray.setOnClickListener {
            onClick.onContainerClick(HomeScreenFragmentDirections.actionHomeScreenToPrayFragment())
        }
        binding.favorite.setOnClickListener{
            onClick.onContainerClick(HomeScreenFragmentDirections.actionHomeScreenToFavoriteFragment())
        }
    }
    companion object{
        fun from(viewGroup: ViewGroup):AthkarContainerViewHolder{
            val inflater = LayoutInflater.from(viewGroup.context)
            val binding = AthkarContainerBinding.inflate(inflater,viewGroup,false)
            return AthkarContainerViewHolder(binding)
        }
    }

}
class HomeScreenViewHolder(private val binding: AthkarItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(athkar: AthkarCategory, onClick: HomeScreenEvents) {
        binding.view.setBackgroundColor(athkar.setColorAthkar())
        binding.materialButton.setTextColor(athkar.setColorAthkar())
        binding.materialButton.text = athkar.nameAthkar
        binding.materialButton.rippleColor =
            ColorStateList.valueOf(ColorUtils.setAlphaComponent(athkar.setColorAthkar(), 90))

        onClick.onAthkerClick(athkar)
    }

    companion object {
        fun from(viewGroup: ViewGroup): HomeScreenViewHolder {
            val inflater = LayoutInflater.from(viewGroup.context)
            val binding = AthkarItemBinding.inflate(inflater, viewGroup, false)
            return HomeScreenViewHolder(binding)
        }
    }
}