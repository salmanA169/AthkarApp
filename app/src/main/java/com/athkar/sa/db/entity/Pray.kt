package com.athkar.sa.db.entity

import androidx.annotation.DrawableRes

data class PrayInfo(
    val date: Long,
    val city: String,
    // save time seconds
    val fajer: Long,
    val sunRise: Long,
    val duhar: Long,
    val asar: Long,
    val maghrab: Long,
    val isha: Long,
)

enum class PrayName(val namePray: String) {
    FAJAR("الفجر"), SUNRISE("الشروق"), DUHAR("الظهر"), ASAR("العصر"), MAGHRAB("المغرب"), ISHA("العشاء");
    fun orderPray():PrayName {
        return when(this){
            FAJAR -> SUNRISE
            SUNRISE -> DUHAR
            DUHAR -> ASAR
            ASAR -> MAGHRAB
            MAGHRAB -> ISHA
            ISHA -> FAJAR
        }
    }
}
data class DateToday(val date:Long,val city: String)
data class Pray(
    val prayName: PrayName,
    val timePray: Long,
    @DrawableRes val icon: Int,
    val isNotify: Boolean,
    var isNextPray: Boolean = false,
)




