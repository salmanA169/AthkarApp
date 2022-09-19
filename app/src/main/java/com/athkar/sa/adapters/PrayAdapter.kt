package com.athkar.sa.adapters

import android.content.res.ColorStateList
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.athkar.sa.R
import com.athkar.sa.databinding.PrayItemRvBinding
import com.athkar.sa.db.entity.PrayItems
import com.athkar.sa.db.entity.PrayName
import com.athkar.sa.uitls.ConstantPatternsDate
import java.time.LocalTime

private typealias eventPray = (prayName: PrayName) -> Unit

class PrayAdapter(private val eventPray: eventPray) :
    ListAdapter<PrayItems, PrayViewHolder>(PrayDifUtil()) {
    class PrayDifUtil : DiffUtil.ItemCallback<PrayItems>() {
        override fun areItemsTheSame(oldItem: PrayItems, newItem: PrayItems): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: PrayItems, newItem: PrayItems): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrayViewHolder {
        return PrayViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: PrayViewHolder, position: Int) {
        val items = getItem(position)
        holder.bind(items, eventPray)
    }
}

class PrayViewHolder(private val binding: PrayItemRvBinding) :
    RecyclerView.ViewHolder(binding.root) {

    init {

    }

    fun bind(pray: PrayItems, eventPray: eventPray) {
        val context = binding.root.context
        val iconNotification = ContextCompat.getDrawable(
            context,
            if (pray.isNotify) {
                R.drawable.enable_notification_icon
            } else {
                R.drawable.disable_notifcation_icon
            }
        )
        binding.notificationPray.setOnClickListener {
            eventPray(pray.prayName)
        }
        binding.notificationPray.icon = iconNotification
        binding.tvTimePray.text =
            LocalTime.ofSecondOfDay(pray.timePray).format(ConstantPatternsDate.prayTimePattern)
        binding.notificationPray.text = pray.prayName.namePray
        binding.shapeableImageView2.setImageResource(pray.icon)
        if (pray.isNextPray) {
//            binding.shapeableImageView2.drawable.setTint(ContextCompat.getColor(context,R.color.currentPrayColor))
//            binding.notificationPray.setTextColor(ContextCompat.getColor(context,R.color.currentPrayColor))
//            binding.notificationPray.setIconTintResource(R.color.currentPrayColor)
//            binding.tvTimePray.setTextColor(ContextCompat.getColor(context,R.color.currentPrayColor))
            val typedValue = TypedValue()
            context.theme.resolveAttribute(
                com.google.android.material.R.attr.colorPrimary,
                typedValue,
                true
            )
            val typedValueSecondary  =TypedValue()
                context.theme.resolveAttribute(
                com.google.android.material.R.attr.colorSecondary,
                typedValueSecondary,
                true
            )
            binding.root.setBackgroundColor(ColorUtils.setAlphaComponent(typedValueSecondary.data,80))
            binding.shapeableImageView2.drawable.setTint(typedValue.data)
            binding.notificationPray.setTextColor(typedValue.data)
            binding.notificationPray.iconTint = ColorStateList.valueOf(typedValue.data)
            binding.tvTimePray.setTextColor(typedValue.data)
        }else{
            val typedValue = TypedValue()
            val typedBackGround = TypedValue()
            context.theme.resolveAttribute(
                com.google.android.material.R.attr.backgroundColor,
                typedBackGround,
                true
            )
            context.theme.resolveAttribute(
                com.google.android.material.R.attr.colorOnBackground,
                typedValue,
                true
            )
            binding.root.setBackgroundColor(typedBackGround.data)
            binding.shapeableImageView2.drawable.setTint(typedValue.data)
            binding.notificationPray.setTextColor(typedValue.data)
            binding.notificationPray.iconTint = ColorStateList.valueOf(typedValue.data)
            binding.tvTimePray.setTextColor(typedValue.data)
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