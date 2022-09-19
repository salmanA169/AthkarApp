package com.athkar.sa.uitls

import android.os.Build
import android.util.TimeUtils
import androidx.annotation.RequiresApi
import com.athkar.sa.R
import com.athkar.sa.db.entity.*
import java.time.*
import java.time.chrono.HijrahDate


fun PrayInfo.getPraysForCalendar(prayNotification: PrayNotification): List<PrayItems> {
    val orderPray = getCurrentOrderPray()
    return listOf(
        PrayItems(PrayName.FAJAR, fajer, R.drawable.fajer_icon, prayNotification.fajer),
        PrayItems(PrayName.SUNRISE, sunRise, R.drawable.sunrise_icon, prayNotification.sunRise),
        PrayItems(PrayName.DUHAR, duhar, R.drawable.duhar_icon, prayNotification.duhar),
        PrayItems(PrayName.ASAR, asar, R.drawable.asar_icon, prayNotification.asar),
        PrayItems(PrayName.MAGHRAB, maghrab, R.drawable.sunset_icon, prayNotification.maghrab),
        PrayItems(PrayName.ISHA, isha, R.drawable.midnight_icon, prayNotification.isha),
    ).onEach {
        if (it.prayName == orderPray.nextPray){
            it.isNextPray = true
        }
    }
}

fun PrayInfo.getDateToday() :DateToday{
    return DateToday(date, city,month,day)
}
fun PrayItems.isNextPray(currentTime: LocalTime = LocalTime.now()): Boolean {
    val timePray = LocalTime.ofSecondOfDay(timePray)
    return currentTime.isBefore(timePray)
}
fun DateToday.parseToHijraDate(): HijrahDate {
    return HijrahDate.from(parseDate())
}
fun PrayInfo.parseDate(): LocalDate {
    return LocalDateTime.ofInstant(Instant.ofEpochSecond(date), ZoneId.systemDefault())
        .toLocalDate()
}
fun DateToday.parseDate(): LocalDate {
    return LocalDateTime.ofInstant(Instant.ofEpochSecond(date), ZoneId.systemDefault())
        .toLocalDate()
}
fun PrayName.updatePrayNotification(prayNotification: PrayNotification): PrayNotification {
    val oldPrayNotification = prayNotification.copy()
    return when (this) {
        PrayName.FAJAR -> prayNotification.copy(fajer = !oldPrayNotification.fajer)
        PrayName.SUNRISE -> prayNotification.copy(sunRise = !oldPrayNotification.sunRise)
        PrayName.DUHAR -> prayNotification.copy(duhar = !oldPrayNotification.duhar)
        PrayName.ASAR -> prayNotification.copy(asar = !oldPrayNotification.asar)
        PrayName.MAGHRAB -> prayNotification.copy(maghrab = !oldPrayNotification.maghrab)
        PrayName.ISHA -> prayNotification.copy(isha = !oldPrayNotification.isha)
    }
}

@RequiresApi(Build.VERSION_CODES.R)
fun List<PrayItems>.calculateNextPrayTime(currentTime: LocalTime = LocalTime.now()): PrayName {
//    val currentTimeSeconds = currentTime.toSecondOfDay()
    var mNextPrayName: PrayName? = null
    val allTimes = map {
        Pair(it.prayName, it.timePray)
    }
    for (i in allTimes) {
//        if (i < currentTimeSeconds){
//            lastPrayTime = i
//        }else{
//            break
//        }
        val currentPray = LocalTime.ofSecondOfDay(i.second)
        val nextPray = find { it.prayName == i.first.getNextPray() }!!
        val nextPrayTime = LocalTime.ofSecondOfDay(nextPray.timePray)
        if (TimeUtils.isTimeBetween(currentTime, currentPray, nextPrayTime)) {
            mNextPrayName = nextPray.prayName
            break
        }
    }
//    val currentPray = find { it.timePray == lastPrayTime }?:find { it.prayName == PrayName.ISHA }!!
    return mNextPrayName!!
}
