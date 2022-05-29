package com.athkar.sa.ui.homeScreen.container.pray.calendar

import android.graphics.Color
import android.util.Log
import android.view.Menu
import androidx.appcompat.view.menu.MenuBuilder
import androidx.core.view.forEach
import androidx.core.view.get
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.athkar.sa.R
import com.athkar.sa.adapters.CalendarAdapter
import com.athkar.sa.adapters.CalendarEvent
import com.athkar.sa.adapters.CalendarItemViewHolder
import com.athkar.sa.databinding.CalendarFragmentBinding
import com.athkar.sa.uitls.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.chrono.HijrahDate
import java.time.temporal.ChronoField
import java.time.temporal.ChronoUnit
import java.time.temporal.Temporal
import java.time.temporal.TemporalField

@AndroidEntryPoint
class CalendarFragment : BaseFragment<CalendarFragmentBinding>({ inflater, container ->
    CalendarFragmentBinding.inflate(inflater, container, false)
}), CalendarEvent {

    override fun onShareClick() {
        TODO("Not yet implemented")
    }

    override fun onScroll(position: Int) {

    }

    var currentTodayIndex = 0
    val calendarAdapter = CalendarAdapter(this)
    val viewModel by viewModels<CalendarViewModel>()
    override fun CalendarFragmentBinding.init() {
        toolbar.overflowIcon?.setTint(Color.WHITE)
        val menuBuilder = toolbar.menu as? MenuBuilder
        menuBuilder?.setOptionalIconsVisible(true)
        val getDate = LocalDate.now()
        val hijrahDate = HijrahDate.from(getDate).get(ChronoField.MONTH_OF_YEAR)
        if (menuBuilder != null) {
            changeMenuItemByMonth(hijrahDate,menuBuilder)
        }
        toolbar.setNavigationOnClickListener {
            controller.popBackStack()
        }
        rvCalendar.adapter = calendarAdapter
    }

    private fun changeMenuItemByMonth(month:Int,menu:Menu) {
        try {
            menu.get(month-1).apply {
                setIcon(R.drawable.mark_icon)
            }
        }catch (e:Exception){
            Log.e("CalendarFragment","error",e)
        }
    }

    override fun observe() {
        viewModel.testPray.observe {
            val shuffleList = it.shuffled()
            shuffleList.forEachIndexed { index, calendarPray ->
                val todayDate = LocalDate.now()
                val prayDate = LocalDate.ofEpochDay(calendarPray.dateToday.date)
                if (todayDate.isEqual(prayDate)) {
                    currentTodayIndex = index
                    return@forEachIndexed
                }
            }
            calendarAdapter.submitList(shuffleList) {
                binding.rvCalendar.scrollToPosition(currentTodayIndex)
            }
        }
    }
}