package com.athkar.sa

import android.os.SystemClock
import androidx.core.app.AlarmManagerCompat
import com.athkar.sa.db.entity.PrayName
import com.athkar.sa.db.entity.PrayNotification
import com.athkar.sa.db.entity.getDefaultPrayNotification
import com.athkar.sa.uitls.formatTimePray
import org.junit.Test
import java.text.NumberFormat
import java.time.*
import java.time.chrono.HijrahChronology
import java.time.temporal.ChronoField
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.time.Duration.Companion.milliseconds

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */


class ExampleUnitTest {


    @Test
    fun testElapsedTimes(){
        val o = NumberFormat.getInstance(Locale.forLanguageTag("AR"))
        println("\u06DD".plus(o.format(10).toString()))
    }
    @Test
    fun test(){
        println(LocalTime.now())
    }
    @Test
    fun TestNextPrayAsarToIsha(){
        val prayNotification = getDefaultPrayNotification()
        val prayName = PrayName.ASAR

        println(prayName.getNextPrays())
        println(prayName.getNextPrayIsEnabledFromNotification(prayNotification))


        val prayNameMagrib = PrayName.MAGHRAB
        println(prayNameMagrib.getNextPrays())
        println(prayNameMagrib.getNextPrayIsEnabledFromNotification(prayNotification))

        val prayNameIsha = PrayName.ISHA
        println(prayNameIsha.getNextPrays())
        println(prayNameIsha.getNextPrayIsEnabledFromNotification(prayNotification))

        val prayNameFajar = PrayName.FAJAR
        println(prayNameFajar.getNextPrays())
        println(prayNameFajar.getNextPrayIsEnabledFromNotification(prayNotification))


    }
    @Test
    fun testBeforTime(){
        val timeNow = LocalTime.parse("00:00")
        val fajarTime = LocalTime.parse("03:44")
        val milles = LocalDateTime.of(LocalDate.now(),timeNow).until(
            LocalDateTime.of(
                LocalDate.now().plusDays(1),
                fajarTime
            ),ChronoUnit.HOURS
        )
        println(milles)
        println(timeNow.isBefore(fajarTime))
    }

    @Test
    fun testElapsedTime() {
        val time = LocalTime.now()
        val time1 =
            LocalDateTime.now().until(
                LocalDateTime.of(
                    LocalDate.now(),
                    LocalTime.parse("03:44")
                )
            ,ChronoUnit.MILLIS
            )
        println(time1.milliseconds.inWholeHours)
//        println(time.until(time1, ChronoUnit.HOURS) / 24)
    }

    @Test
    fun addition_isCorrect() {

        val time = LocalTime.now()
            .until(LocalTime.parse("18:55"), ChronoUnit.MILLIS)

        println(time)

        val timeStamp =
            LocalDateTime.ofInstant(Instant.ofEpochSecond(1646114461), ZoneId.systemDefault())
        val hijre = HijrahChronology.INSTANCE.dateNow()

        println(timeStamp.toString())
        println(hijre.toString())
        println(timeStamp.get(ChronoField.MONTH_OF_YEAR))
        println(
            timeStamp.toEpochSecond(
                OffsetDateTime.now().offset
            )
        )
        println("//////////////")

        val parseFajar = LocalTime.parse("05:30 (+03)".formatTimePray())

        println(parseFajar)

        val getLong = parseFajar.toSecondOfDay()
        println(getLong)
        println(LocalTime.ofSecondOfDay(getLong.toLong()))
    }
}