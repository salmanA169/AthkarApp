package com.athkar.sa.ui.homeScreen.container.pray.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.athkar.sa.db.entity.PrayInfo
import com.athkar.sa.models.CalendarPray
import com.athkar.sa.models.toCalendarPray
import java.time.LocalDate
import java.time.LocalTime

class CalendarViewModel : ViewModel() {
    private val _testPrays = MutableLiveData(testPrayInfo())
    val testPray :LiveData<List<CalendarPray>> = _testPrays

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
            val prayInfoTest = PrayInfo(
                fakeDate,
                "الرياض",
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