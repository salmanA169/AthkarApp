package com.athkar.sa

import com.athkar.sa.db.entity.PrayInfo
import java.time.LocalDate
import java.time.LocalTime

data class PrayUtil(
    private val prayInfo: List<PrayInfo>,
    private val currentDate: LocalDate = LocalDate.now()
) {

    private val getPrayToday = prayInfo.find {
        val getPrayDate = LocalDate.ofEpochDay(it.date)
        getPrayDate == currentDate
    } ?: throw PrayNotFoundException("can not find pray at date $currentDate")


    fun getOrderPray(currentTime: LocalTime = LocalTime.now()): PrayInfo.OrderPray {
        return getPrayToday.getCurrentOrderPray(currentTime)
    }
}

class PrayNotFoundException(override val message: String) : Exception(message)

