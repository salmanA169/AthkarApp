package com.athkar.sa.db.entity

import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime
import java.time.chrono.HijrahDate

class PrayInfoTest {


    @Test
    fun getNotificationPrayDetails() {
        val currentPray = getDefaultPrayInfo()[0]
        val orderPray = currentPray.getCurrentOrderPray(LocalTime.parse("21:41"))
        println(orderPray.currentPrayName.getNextPrays())
        println(orderPray)
        println(
            currentPray.getNotificationPrayDetails(
                getDefaultPrayNotification(),
                LocalTime.parse("21:41")
            )
        )
    }
}

private fun getDefaultPrayInfo(): List<PrayInfo> {
    return buildList {
        add(
            PrayInfo(
                0,
                HijrahDate.from(LocalDate.parse("2022-01-01")).toEpochDay(),
                "Riyadh",
                1,
                1,
                LocalTime.parse("03:44").toSecondOfDay().toLong(),
                LocalTime.parse("05:44").toSecondOfDay().toLong(),
                LocalTime.parse("12:44").toSecondOfDay().toLong(),
                LocalTime.parse("15:44").toSecondOfDay().toLong(),
                LocalTime.parse("18:44").toSecondOfDay().toLong(),
                LocalTime.parse("20:44").toSecondOfDay().toLong(),
            )
        )
        add(
            PrayInfo(
                0,
                HijrahDate.from(LocalDate.parse("2022-01-02")).toEpochDay(),
                "Riyadh",
                1,
                1,
                LocalTime.parse("03:44").toSecondOfDay().toLong(),
                LocalTime.parse("05:44").toSecondOfDay().toLong(),
                LocalTime.parse("12:44").toSecondOfDay().toLong(),
                LocalTime.parse("15:44").toSecondOfDay().toLong(),
                LocalTime.parse("18:44").toSecondOfDay().toLong(),
                LocalTime.parse("20:44").toSecondOfDay().toLong(),
            )
        )
        add(
            PrayInfo(
                0,
                HijrahDate.from(LocalDate.parse("2022-01-03")).toEpochDay(),
                "Riyadh",
                1,
                1,
                LocalTime.parse("03:44").toSecondOfDay().toLong(),
                LocalTime.parse("05:44").toSecondOfDay().toLong(),
                LocalTime.parse("12:44").toSecondOfDay().toLong(),
                LocalTime.parse("15:44").toSecondOfDay().toLong(),
                LocalTime.parse("18:44").toSecondOfDay().toLong(),
                LocalTime.parse("20:44").toSecondOfDay().toLong(),
            )
        )
        add(
            PrayInfo(
                0,
                HijrahDate.from(LocalDate.parse("2022-01-04")).toEpochDay(),
                "Riyadh",
                1,
                1,
                LocalTime.parse("03:44").toSecondOfDay().toLong(),
                LocalTime.parse("05:44").toSecondOfDay().toLong(),
                LocalTime.parse("12:44").toSecondOfDay().toLong(),
                LocalTime.parse("15:44").toSecondOfDay().toLong(),
                LocalTime.parse("18:44").toSecondOfDay().toLong(),
                LocalTime.parse("20:44").toSecondOfDay().toLong(),
            )
        )

    }
}
