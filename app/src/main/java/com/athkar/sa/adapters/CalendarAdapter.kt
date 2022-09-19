package com.athkar.sa.adapters

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.RippleDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.ListPopupWindow
import androidx.appcompat.widget.PopupMenu
import androidx.core.graphics.ColorUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.athkar.sa.R
import com.athkar.sa.databinding.CalendarItemBinding
import com.athkar.sa.models.CalendarPray
import com.athkar.sa.uitls.ConstantPatternsDate
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.chrono.HijrahDate

 interface CalendarEvent{
     fun onShareClick()
     fun onScroll(position: Int)
}
class CalendarAdapter(private val calendarEvent: CalendarEvent):ListAdapter<CalendarPray,CalendarItemViewHolder>(CalendarUtil()) {
    class CalendarUtil:DiffUtil.ItemCallback<CalendarPray>(){
        override fun areItemsTheSame(oldItem: CalendarPray, newItem: CalendarPray): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: CalendarPray, newItem: CalendarPray): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarItemViewHolder {
        return CalendarItemViewHolder.from(parent)
    }


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onBindViewHolder(holder: CalendarItemViewHolder, position: Int) {
        val items = getItem(position)
        holder.bind(items,calendarEvent)

    }
}

class CalendarItemViewHolder(private val binding:CalendarItemBinding):RecyclerView.ViewHolder(binding.root){

    // TODO: show menu for image icon
    fun bind(calendarPray:CalendarPray,calendarEvent: CalendarEvent){
        val currentDate = LocalDate.now()

        val dateToday =  LocalDateTime.ofInstant(Instant.ofEpochSecond(calendarPray.dateToday.date),
            ZoneId.systemDefault()).toLocalDate()
        val todayHijrahDate = HijrahDate.from(currentDate)
        val hijrahDate = HijrahDate.from(dateToday)
        binding.tvToday.text = dateToday.format(ConstantPatternsDate.todayPattern)
        binding.tvDateAr.text = hijrahDate.format(ConstantPatternsDate.hijrahPattern)
        binding.tvDateEn.text = dateToday.toString()

        binding.tvFajarTime.text  = calendarPray.fajarTime
        binding.tvSunriseTime.text = calendarPray.sunRiseTime
        binding.tvDuharTime.text = calendarPray.duharTime
        binding.tvAsarTime.text = calendarPray.asarTime
        binding.tvMaghrbTime.text = calendarPray.maghrabTime
        binding.tvIshaTime.text = calendarPray.ishaTime
        binding.imageShare.setOnClickListener {

        }

        if (todayHijrahDate.isEqual(hijrahDate)){
            val context = binding.root.context
            val changeAlphaColor = ColorUtils.setAlphaComponent(context.getColor(R.color.md_theme_light_primary),120)
            binding.materialCardView5.setCardForegroundColor(ColorStateList.valueOf(changeAlphaColor))
        }else{
            binding.materialCardView5.setCardForegroundColor(null)
        }
    }
    companion object{
        fun from(parent:ViewGroup):CalendarItemViewHolder{
            val inflater = LayoutInflater.from(parent.context)
            val binding = CalendarItemBinding.inflate(inflater,parent,false)
            return CalendarItemViewHolder(binding)
        }
    }
}