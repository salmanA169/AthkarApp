package com.athkar.sa.db.entity

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

const val PRAY_NOTIFICATION_ID = 1

@Entity
data class PrayNotification(
    @PrimaryKey(autoGenerate = false)
    val id: Int = PRAY_NOTIFICATION_ID,
    val fajer: Boolean,
    val sunRise: Boolean,
    val duhar: Boolean,
    val asar: Boolean,
    val maghrab: Boolean,
    val isha: Boolean,
){
    @Ignore
    private val allNotification = booleanArrayOf(fajer,sunRise,duhar,asar,maghrab,isha)
    fun hasNotification()  = allNotification.any { it }

    fun getNotificationByPrayName(prayName:PrayName):Boolean{
        return when(prayName){
            PrayName.FAJAR -> fajer
            PrayName.SUNRISE -> sunRise
            PrayName.DUHAR -> duhar
            PrayName.ASAR -> asar
            PrayName.MAGHRAB -> maghrab
            PrayName.ISHA -> isha
        }
    }
}

fun getDefaultPrayNotification() = PrayNotification(
    fajer = true,
    sunRise = false,
    duhar = false,
    asar = true,
    maghrab = false,
    isha = false
)
