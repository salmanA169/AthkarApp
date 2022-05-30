package com.athkar.sa.models

import com.athkar.sa.db.entity.DateToday
import com.athkar.sa.db.entity.PrayInfo
import com.athkar.sa.uitls.ConstantPatternsDate
import java.time.LocalTime

data class CalendarPray(
    val dateToday:DateToday,
    val fajarTime: String,
    val sunRiseTime: String,
    val duharTime: String,
    val asarTime: String,
    val maghrabTime: String,
    val ishaTime: String
){
    var isToday = false
}

fun PrayInfo.toCalendarPray(): CalendarPray {
    val fajarTimeFormat =
        LocalTime.ofSecondOfDay(fajer).format(ConstantPatternsDate.prayTimePattern1)
    val sunRiseTimeFormat =
        LocalTime.ofSecondOfDay(sunRise).format(ConstantPatternsDate.prayTimePattern1)
    val duharTimeFormat =
        LocalTime.ofSecondOfDay(duhar).format(ConstantPatternsDate.prayTimePattern1)
    val asarTimeFormat = LocalTime.ofSecondOfDay(asar).format(ConstantPatternsDate.prayTimePattern1)
    val maghrabTimeFormat =
        LocalTime.ofSecondOfDay(maghrab).format(ConstantPatternsDate.prayTimePattern1)
    val ishaTimeFormat = LocalTime.ofSecondOfDay(isha).format(ConstantPatternsDate.prayTimePattern1)
    return CalendarPray(
        DateToday(date, city),
        fajarTimeFormat,
        sunRiseTimeFormat,
        duharTimeFormat,
        asarTimeFormat,
        maghrabTimeFormat,
        ishaTimeFormat
    )
}