package com.athkar.sa.ui.homeScreen.container.pray.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.athkar.sa.db.entity.PrayInfo
import com.athkar.sa.models.CalendarPray
import com.athkar.sa.models.toCalendarPray
import com.athkar.sa.repo.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    val calendarPray = repository.getPrayInfo().map {
        it.map {
            it.toCalendarPray()
        }
    }
}


fun testPrayInfo():List<CalendarPray>{
    return  buildList<PrayInfo> {
        for (i in 0..29){
            val fakeDate = LocalDate.now().plusDays(i.toLong()).toEpochDay()
            val fakeFajer = LocalTime.parse("03:44").toSecondOfDay().toLong()
            val fakeSunrise = LocalTime.parse("05:10").toSecondOfDay().toLong()
            val fakeDuhar = LocalTime.parse("11:49").toSecondOfDay().toLong()
            val fakeAsar = LocalTime.parse("15:15").toSecondOfDay().toLong()
            val fakeMughrab = LocalTime.parse("18:28").toSecondOfDay().toLong()
            val fakeIsha = LocalTime.parse("19:58").toSecondOfDay().toLong()
            val prayInfoTest = PrayInfo(0,
                fakeDate,
                "الرياض",
                0,0,
                fakeFajer,
                fakeSunrise,
                fakeDuhar,
                fakeAsar,
                fakeMughrab,
                fakeIsha
            )
            add(prayInfoTest)
        }
    }.map {
        it.toCalendarPray()
    }

}