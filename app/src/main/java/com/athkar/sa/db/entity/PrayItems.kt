package com.athkar.sa.db.entity

import androidx.annotation.DrawableRes
import androidx.room.Entity
import com.athkar.sa.uitls.ConstantPatternsDate
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit

@Entity(
    ignoredColumns = arrayOf(
        "fajarFormatTime",
        "sunRiseFormatTime",
        "duharFormatTime",
        "asarFormatTime",
        "magrabFormatTime",
        "ishaFormatTime",
        "getAllTimes"
    ), primaryKeys = ["id", "date"]
)
data class PrayInfo(
    val id: Int,
    val date: Long,
    val city: String,
    // save in Hijre date
    val month: Int,
    val day: Int,
    // save time seconds
    val fajer: Long,
    val sunRise: Long,
    val duhar: Long,
    val asar: Long,
    val maghrab: Long,
    val isha: Long,
) {

    data class OrderPray(
        val currentPrayName: PrayName,
        val currentTimePray: Long,
        val currentTimePrayTimeFormat: String,
        val nextPray: PrayName,
        val nextPrayTime: Long,
        val nextPrayTimeFormat: String,
    )

    data class NotificationPrayDetails(
        val prayName: PrayName,
       private val timeEpochSecond: Long,
    ){
        fun calculateTriggerTimeMillieByPrayName():Long{
            val currentTime = LocalTime.now()
            val nextPrayTimeParse = LocalTime.ofSecondOfDay(timeEpochSecond)

            val calculateTimes =
                if (prayName == PrayName.FAJAR && !currentTime.isBefore(nextPrayTimeParse)) {
                    LocalDateTime.now().until(
                        LocalDateTime.of(
                            LocalDate.now().plusDays(1),
                            nextPrayTimeParse
                        ), ChronoUnit.MILLIS
                    )
                } else {
                    currentTime.until(nextPrayTimeParse, ChronoUnit.MILLIS)
                }
            if (calculateTimes >= 0) {
                return calculateTimes
            } else {
                throw Exception("Can not schedule times with negative time $calculateTimes")
            }
        }
    }

    private val fajarFormatTime =
        LocalTime.ofSecondOfDay(fajer).format(ConstantPatternsDate.prayTimePattern)
    private val sunRiseFormatTime =
        LocalTime.ofSecondOfDay(sunRise).format(ConstantPatternsDate.prayTimePattern)
    private val duharFormatTime =
        LocalTime.ofSecondOfDay(duhar).format(ConstantPatternsDate.prayTimePattern)
    private val asarFormatTime =
        LocalTime.ofSecondOfDay(asar).format(ConstantPatternsDate.prayTimePattern)
    private val magrabFormatTime =
        LocalTime.ofSecondOfDay(maghrab).format(ConstantPatternsDate.prayTimePattern)
    private val ishaFormatTime =
        LocalTime.ofSecondOfDay(isha).format(ConstantPatternsDate.prayTimePattern)

    private val getAllTimes = listOf(
        OrderPray(
            PrayName.FAJAR,
            fajer,
            fajarFormatTime,
            PrayName.SUNRISE,
            sunRise,
            sunRiseFormatTime
        ),
        OrderPray(
            PrayName.SUNRISE,
            sunRise,
            sunRiseFormatTime,
            PrayName.DUHAR,
            duhar,
            duharFormatTime
        ),
        OrderPray(PrayName.DUHAR, duhar, duharFormatTime, PrayName.ASAR, asar, asarFormatTime),
        OrderPray(PrayName.ASAR, asar, asarFormatTime, PrayName.MAGHRAB, maghrab, magrabFormatTime),
        OrderPray(PrayName.MAGHRAB, maghrab, magrabFormatTime, PrayName.ISHA, isha, ishaFormatTime),
        OrderPray(PrayName.ISHA, isha, ishaFormatTime, PrayName.FAJAR, fajer, fajarFormatTime),
    )

    fun getCurrentOrderPray(currentTime: LocalTime = LocalTime.now()): OrderPray {
        return getAllTimes.findLast {
            val getPrayTime = LocalTime.ofSecondOfDay(it.currentTimePray)
            currentTime.isAfter(getPrayTime)
        } ?: OrderPray(PrayName.ISHA, isha, ishaFormatTime, PrayName.FAJAR, fajer, fajarFormatTime)
    }

    private fun getTimePrayByName(prayName: PrayName):Long{
        return when(prayName){
            PrayName.FAJAR -> fajer
            PrayName.SUNRISE -> sunRise
            PrayName.DUHAR -> duhar
            PrayName.ASAR -> asar
            PrayName.MAGHRAB -> maghrab
            PrayName.ISHA -> isha
        }
    }
    fun getNotificationPrayDetails(prayNotification: PrayNotification,localTime: LocalTime = LocalTime.now()): NotificationPrayDetails? {
        return if (prayNotification.hasNotification()) {
            val currentPray = getCurrentOrderPray(localTime)
            val getPrayNotification =
                currentPray.currentPrayName.getNextPrayIsEnabledFromNotification(prayNotification)
            if (getPrayNotification != null) {
                NotificationPrayDetails(getPrayNotification, getTimePrayByName(getPrayNotification))
            } else {
                null
            }
        } else {
            null
        }
    }
}


enum class PrayName(val namePray: String) {
    FAJAR("الفجر"), SUNRISE("الشروق"), DUHAR("الظهر"), ASAR("العصر"), MAGHRAB("المغرب"), ISHA("العشاء");

    fun getNextPrays(): List<PrayName> {
        val praysNames = mutableListOf<PrayName>()
        for (i in values()) {
            if (this.ordinal < i.ordinal) {
                praysNames.add(i)
            }
        }
        if (this == ISHA) {
            praysNames.add(FAJAR)
        }
        return praysNames
    }

    fun getNextPrayIsEnabledFromNotification(prayNotification: PrayNotification): PrayName? {
        val getPray = getNextPrays()
        if (getPray.isEmpty()) {
            return null
        } else {
            return getPray.find {
                prayNotification.getNotificationByPrayName(it)
            }
        }
    }

    fun getLeftPray(): PrayName {
        return when (this) {
            FAJAR -> ISHA
            SUNRISE -> FAJAR
            DUHAR -> SUNRISE
            ASAR -> DUHAR
            MAGHRAB -> ASAR
            ISHA -> MAGHRAB
        }
    }

    fun getNextPray(): PrayName {
        return when (this) {
            FAJAR -> SUNRISE
            SUNRISE -> DUHAR
            DUHAR -> ASAR
            ASAR -> MAGHRAB
            MAGHRAB -> ISHA
            ISHA -> FAJAR
        }
    }
}

data class DateToday(val date: Long, val city: String, val month: Int, val day: Int)

data class PrayItems(
    val prayName: PrayName,
    val timePray: Long,
    @DrawableRes val icon: Int,
    val isNotify: Boolean,
    var isNextPray: Boolean = false,
)





