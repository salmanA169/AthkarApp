package com.athkar.sa.adapters

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.athkar.sa.R
import com.athkar.sa.databinding.PrayItemRvBinding
import com.athkar.sa.db.entity.Pray
import com.athkar.sa.uitls.ConstantPatternsDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class PrayAdapter : ListAdapter<Pray, PrayViewHolder>(PrayDifUtil()) {
    class PrayDifUtil : DiffUtil.ItemCallback<Pray>() {
        override fun areItemsTheSame(oldItem: Pray, newItem: Pray): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Pray, newItem: Pray): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrayViewHolder {
        return PrayViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: PrayViewHolder, position: Int) {
        val items = getItem(position)
        holder.bind(items)
    }
}

class PrayViewHolder(private val binding: PrayItemRvBinding) :
    RecyclerView.ViewHolder(binding.root) {


    fun bind(pray: Pray) {
        val context = binding.root.context
        val iconNotification = ContextCompat.getDrawable(
            context,
            if (pray.isNotify){
                R.drawable.enable_notification_icon
            }else{
                R.drawable.disable_notifcation_icon
            }
        )
        binding.notificationPray.icon = iconNotification
        binding.tvTimePray.text = LocalTime.ofSecondOfDay(pray.timePray).format(ConstantPatternsDate.prayTimePattern)
        binding.notificationPray.text = pray.prayName.namePray
        binding.shapeableImageView2.setImageResource(pray.icon)
        if (pray.isNextPray){
            binding.shapeableImageView2.drawable.setTint(ContextCompat.getColor(context,R.color.currentPrayColor))
            binding.notificationPray.setTextColor(ContextCompat.getColor(context,R.color.currentPrayColor))
            binding.notificationPray.setIconTintResource(R.color.currentPrayColor)
            binding.tvTimePray.setTextColor(ContextCompat.getColor(context,R.color.currentPrayColor))
        }
    }

    companion object {
        fun from(parent: ViewGroup): PrayViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = PrayItemRvBinding.inflate(inflater, parent, false)
            return PrayViewHolder(binding)
        }
    }
}