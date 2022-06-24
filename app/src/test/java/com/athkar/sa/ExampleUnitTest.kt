package com.athkar.sa

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.athkar.sa.db.entity.PrayInfo
import org.junit.Rule
import org.junit.Test
import java.time.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

class ExampleUnitTest {

    @get:Rule
    var instant = InstantTaskExecutorRule()

    @Test
    fun addition_isCorrect() {
        val fakeFajer = LocalTime.parse("03:44").toSecondOfDay().toLong()
        val fakeSunrise = LocalTime.parse("05:10").toSecondOfDay().toLong()
        val fakeDuhar = LocalTime.parse("11:49").toSecondOfDay().toLong()
        val fakeAsar = LocalTime.parse("15:15").toSecondOfDay().toLong()
        val fakeMughrab = LocalTime.parse("18:28").toSecondOfDay().toLong()
        val fakeIsha = LocalTime.parse("19:58").toSecondOfDay().toLong()
        val currentTime = LocalTime.parse("19:00").toSecondOfDay()

        val testList = listOf(
            PrayInfo(
                LocalDate.parse("2022-06-24").toEpochDay(),
                "",
                fakeFajer,
                fakeSunrise,
                fakeDuhar,
                fakeAsar,
                fakeMughrab,
                fakeIsha
            ),
            PrayInfo(
                LocalDate.parse("2022-06-25").toEpochDay(),
                "",
                fakeFajer,
                fakeSunrise,
                fakeDuhar,
                fakeAsar,
                fakeMughrab,
                fakeIsha
            )
        )
        val prayUtil = PrayUtil(testList)

        println("current order ${prayUtil.getOrderPray(LocalTime.parse("12:00"))}")
        println(currentTime)

    }
}