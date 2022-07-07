package com.athkar.sa

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Rule
import org.junit.Test
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.chrono.HijrahChronology

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
        val timeStamp =
            LocalDateTime.ofInstant(Instant.ofEpochSecond(1641016861), ZoneId.systemDefault())
        val hijre = HijrahChronology.INSTANCE.dateNow()

        println(timeStamp.toString())
        println(hijre.toString())
    }
}