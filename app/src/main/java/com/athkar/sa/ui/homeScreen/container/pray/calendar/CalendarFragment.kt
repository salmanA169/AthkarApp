package com.athkar.sa.ui.homeScreen.container.pray.calendar

import android.graphics.Color
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.Menu
import androidx.appcompat.view.menu.MenuBuilder
import androidx.core.content.ContextCompat
import androidx.core.text.set
import androidx.core.text.toSpannable
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
import com.athkar.sa.db.entity.PrayInfo
import com.athkar.sa.models.CalendarPray
import com.athkar.sa.uitls.BaseFragment
import com.athkar.sa.uitls.formatMonthToString
import com.athkar.sa.uitls.parseDate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.*
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

    }

    override fun onScroll(position: Int) {

    }

    var currentTodayIndex = 0
    private val calendarAdapter = CalendarAdapter(this)
    private val viewModel by viewModels<CalendarViewModel>()
    private var praysInfo = mutableSetOf<CalendarPray>()
    override fun CalendarFragmentBinding.init() {
        val menuBuilder = toolbar.menu as? MenuBuilder
        menuBuilder?.setOptionalIconsVisible(true)
        val getDate = LocalDate.now()
        val hijrahDate = HijrahDate.from(getDate).get(ChronoField.MONTH_OF_YEAR)
        if (menuBuilder != null) {
            changeMenuItemByMonth(hijrahDate, menuBuilder)
        }
        toolbar.setNavigationOnClickListener {
            controller.popBackStack()
        }
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.m1 -> {
                    setAdapterByMonth(1)
                    true
                }
                R.id.m2 -> {
                    setAdapterByMonth(2)
                    true
                }
                R.id.m3 -> {
                    setAdapterByMonth(3)

                    true
                }
                R.id.m4 -> {
                    setAdapterByMonth(4)

                    true
                }
                R.id.m5 -> {
                    setAdapterByMonth(5)

                    true
                }
                R.id.m6 -> {
                    setAdapterByMonth(6)

                    true
                }
                R.id.m7 -> {
                    setAdapterByMonth(7)

                    true
                }
                R.id.m8 -> {
                    setAdapterByMonth(8)

                    true
                }
                R.id.m9 -> {
                    setAdapterByMonth(9)

                    true
                }
                R.id.m10 -> {
                    setAdapterByMonth(10)
                    true
                }

                R.id.m11 -> {
                    setAdapterByMonth(11)
                    true
                }
                R.id.m12 -> {
                    setAdapterByMonth(12)

                    true
                }
                else -> {
                    false
                }
            }
        }
        rvCalendar.adapter = calendarAdapter
    }

    private fun setAdapterByMonth(month: Int) {
        val filterList = praysInfo.filter {
            it.month == month
        }
        changeTitleToolbar(month)
        calendarAdapter.submitList(filterList)
    }

    private fun changeMenuItemByMonth(month: Int, menu: Menu) {
        try {
            menu.get(month - 1).apply {
                setIcon(R.drawable.mark_icon)
            }
        } catch (e: Exception) {
            Log.e("CalendarFragment", "error", e)
        }
    }

    override fun observe() {
        viewModel.calendarPray.observeFlow {
            val currentDate = HijrahDate.now()
            val currentMonth = currentDate.get(ChronoField.MONTH_OF_YEAR)
            praysInfo.addAll(it)
            val shuffleList = it.filter {
                it.month == currentMonth
            }
            changeTitleToolbar(currentMonth)
            shuffleList.forEachIndexed { index, calendarPray ->
                val prayDate = calendarPray.dateToday.parseDate()
                if (currentDate.isEqual(prayDate)) {
                    currentTodayIndex = index
                    return@forEachIndexed
                }
            }
            calendarAdapter.submitList(shuffleList) {
                binding.rvCalendar.scrollToPosition(currentTodayIndex)

            }
        }
    }

    private fun changeTitleToolbar(month: Int) {
        val monthFormat = month.formatMonthToString()
        val text = "مواقيت الصلاة لشهر $monthFormat"
        val span = text.toSpannable()
        val firstIndex = text.indexOf(monthFormat)
        span.set(
            firstIndex,
            text.length,
            ForegroundColorSpan(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.md_theme_light_primary
                )
            )
        )
        binding.toolbar.title = span
    }
}