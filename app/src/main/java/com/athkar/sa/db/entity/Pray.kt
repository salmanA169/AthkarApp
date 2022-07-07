package com.athkar.sa.db.entity

import androidx.annotation.DrawableRes
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.athkar.sa.PrayNotFoundException
import com.athkar.sa.uitls.ConstantPatternsDate
import java.time.LocalTime

@Entity(
    ignoredColumns = arrayOf(
        "fajarFormatTime",
        "sunRiseFormatTime",
        "duharFormatTime",
        "asarFormatTime",
        "magrabFormatTime",
        "ishaFormatTime",
        "getAllTimes"
    )
)
data class PrayInfo(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val date: Long,
    val city: String,
    val month: Int,
    // save time seconds
    val fajer: Long,
    val sunRise: Long,
    val duhar: Long,
    val asar: Long,
    val maghrab: Long,
    val isha: Long,
) {


    // TODO: improve it later
    data class OrderPray(
        val currentPrayName: PrayName,
        val currentTimePray: Long,
        val currentTimePrayTimeFormat: String,
        val nextPray: PrayName,
        val nextPrayTime: Long,
        val nextPrayTimeFormat: String
    )

    val fajarFormatTime =
        LocalTime.ofSecondOfDay(fajer).format(ConstantPatternsDate.prayTimePattern)
    val sunRiseFormatTime =
        LocalTime.ofSecondOfDay(sunRise).format(ConstantPatternsDate.prayTimePattern)
    val duharFormatTime =
        LocalTime.ofSecondOfDay(duhar).format(ConstantPatternsDate.prayTimePattern)
    val asarFormatTime = LocalTime.ofSecondOfDay(asar).format(ConstantPatternsDate.prayTimePattern)
    val magrabFormatTime =
        LocalTime.ofSecondOfDay(maghrab).format(ConstantPatternsDate.prayTimePattern)
    val ishaFormatTime = LocalTime.ofSecondOfDay(isha).format(ConstantPatternsDate.prayTimePattern)

    val getAllTimes = listOf(
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
}

enum class PrayName(val namePray: String) {
    FAJAR("الفجر"), SUNRISE("الشروق"), DUHAR("الظهر"), ASAR("العصر"), MAGHRAB("المغرب"), ISHA("العشاء");

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

data class DateToday(val date: Long, val city: String)

data class Pray(
    val prayName: PrayName,
    val timePray: Long,
    @DrawableRes val icon: Int,
    val isNotify: Boolean,
    var isNextPray: Boolean = false,
)




