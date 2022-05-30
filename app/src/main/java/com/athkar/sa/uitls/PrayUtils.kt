package com.athkar.sa.uitls

import android.os.Build
import android.util.TimeUtils
import androidx.annotation.RequiresApi
import androidx.core.util.toAndroidXPair
import com.athkar.sa.R
import com.athkar.sa.db.entity.*
import java.time.LocalTime


fun PrayInfo.getPrays(prayNotification: PrayNotification):List<Pray>{
    return listOf(
        Pray(PrayName.FAJAR,fajer, R.drawable.fajer_icon,prayNotification.fajer),
        Pray(PrayName.SUNRISE,sunRise, R.drawable.sunrise_icon,prayNotification.sunRise),
        Pray(PrayName.DUHAR,duhar, R.drawable.duhar_icon,prayNotification.duhar),
        Pray(PrayName.ASAR,asar, R.drawable.asar_icon,prayNotification.asar),
        Pray(PrayName.MAGHRAB,maghrab, R.drawable.sunset_icon,prayNotification.maghrab),
        Pray(PrayName.ISHA,isha, R.drawable.midnight_icon,prayNotification.isha),
    )
}

fun PrayInfo.getDateToday() = DateToday(date,city)
fun Pray.isNextPray(currentTime:LocalTime = LocalTime.now()):Boolean{
    val timePray = LocalTime.ofSecondOfDay(timePray)
    return currentTime.isBefore(timePray)
}

@RequiresApi(Build.VERSION_CODES.R)
fun List<Pray>.calculateNextPrayTime(currentTime: LocalTime = LocalTime.now()):PrayName{
//    val currentTimeSeconds = currentTime.toSecondOfDay()
    var mNextPrayName :PrayName ? =null
    val allTimes = map {
        Pair(it.prayName,it.timePray)
    }
    for (i in allTimes){
//        if (i < currentTimeSeconds){
//            lastPrayTime = i
//        }else{
//            break
//        }
        val currentPray  = LocalTime.ofSecondOfDay(i.second)
        val nextPray = find {it.prayName == i.first.orderPray()}!!
        val nextPrayTime = LocalTime.ofSecondOfDay(nextPray.timePray)
        if (TimeUtils.isTimeBetween(currentTime,currentPray,nextPrayTime)){
            mNextPrayName = nextPray.prayName
            break
        }
    }
//    val currentPray = find { it.timePray == lastPrayTime }?:find { it.prayName == PrayName.ISHA }!!
    return mNextPrayName!!
}
