package com.athkar.sa.uitls

import com.athkar.sa.db.entity.PrayInfo
import com.athkar.sa.db.entity.PrayName
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit

fun PrayInfo.OrderPray.calculateFromCurrentTimeToNextPrayTimeInMillis(): Long {
    val currentTime = LocalTime.now()
    val nextPrayTimeParse = LocalTime.ofSecondOfDay(nextPrayTime)

    val calculateTimes =
        if (nextPray == PrayName.FAJAR && !currentTime.isBefore(nextPrayTimeParse)) {
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
        throw Exception("Can not set times with negative time $calculateTimes")
    }
}