package com.athkar.sa

import android.util.TimeUtils
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.athkar.sa.db.entity.PrayName
import com.athkar.sa.ui.homeScreen.container.pray.PrayViewModel
import com.athkar.sa.uitls.calculateNextPrayTime
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import java.time.*
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.DurationUnit

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
        val viewModel = PrayViewModel()
        val listPray = viewModel.prays.getOrAwaitValue()
        val nextPRay = listPray.find { it.prayName == PrayName.FAJAR }!!

        val date1 =
            LocalDateTime.now().until(LocalDateTime.of(2022, 5, 16, 3, 44), ChronoUnit.MILLIS)
        Calendar.getInstance().timeInMillis
        println(Date(date1))
        assertEquals(4, 2 + 2)

    }
}