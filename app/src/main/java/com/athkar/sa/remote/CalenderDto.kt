package com.athkar.sa.remote

import androidx.annotation.Keep
import com.athkar.sa.db.entity.PrayInfo
import com.athkar.sa.uitls.formatTimePray
import com.google.gson.annotations.SerializedName
import retrofit2.http.Field
import java.time.*
import java.time.chrono.HijrahDate
import java.time.temporal.ChronoField
import java.time.temporal.TemporalField

@Keep
data class ResponseCalendar(
    @SerializedName("data")
    val data: CalenderDto
)

@Keep
data class CalenderDto(
    @SerializedName("1")
    val january: List<TimingPrayDto>,
    @SerializedName("2")
    val february: List<TimingPrayDto>,
    @SerializedName("3")
    val march: List<TimingPrayDto>,
    @SerializedName("4")
    val april: List<TimingPrayDto>,
    @SerializedName("5")
    val may: List<TimingPrayDto>,
    @SerializedName("6")
    val june: List<TimingPrayDto>,
    @SerializedName("7")
    val july: List<TimingPrayDto>,
    @SerializedName("8")
    val august: List<TimingPrayDto>,
    @SerializedName("9")
    val september: List<TimingPrayDto>,
    @SerializedName("10")
    val october: List<TimingPrayDto>,
    @SerializedName("11")
    val november: List<TimingPrayDto>,
    @SerializedName("12")
    val december: List<TimingPrayDto>,
)

fun TimingPrayDto.toPrayInfo(city: String): PrayInfo {
    val parseDate = LocalDateTime.ofInstant(
        Instant.ofEpochSecond(date.timestamp),
        ZoneId.systemDefault()
    )
    val hijrahDate = HijrahDate.from(parseDate)
    val parseFajar = LocalTime.parse(timings.Fajr.formatTimePray()).toSecondOfDay().toLong()
    val parseSunRise = LocalTime.parse(timings.Sunrise.formatTimePray()).toSecondOfDay().toLong()
    val parseDhuhr = LocalTime.parse(timings.Dhuhr.formatTimePray()).toSecondOfDay().toLong()
    val parseAsar = LocalTime.parse(timings.Asr.formatTimePray()).toSecondOfDay().toLong()
    val parseMagrib = LocalTime.parse(timings.Maghrib.formatTimePray()).toSecondOfDay().toLong()
    val parseIsha = LocalTime.parse(timings.Isha.formatTimePray()).toSecondOfDay().toLong()
    return PrayInfo(
        0,
        parseDate.toEpochSecond(OffsetDateTime.now().offset),
        city,
        hijrahDate.get(ChronoField.MONTH_OF_YEAR),
        hijrahDate.get(ChronoField.DAY_OF_MONTH),
        parseFajar,
        parseSunRise,
        parseDhuhr,
        parseAsar,
        parseMagrib,
        parseIsha
    )
}

fun CalenderDto.getAnnualTimes() = buildList {
    addAll(january)
    addAll(february)
    addAll(march)
    addAll(april)
    addAll(may)
    addAll(june)
    addAll(july)
    addAll(august)
    addAll(september)
    addAll(october)
    addAll(november)
    addAll(december)

}

@Keep
data class TimingPrayDto(
    val timings: PrayInfoDto,
    val date: DateTimeStampDto
)
@Keep
data class DateTimeStampDto(
    val timestamp: Long
)
@Keep
data class PrayInfoDto(
    val Asr: String,
    val Dhuhr: String,
    val Fajr: String,
    val Imsak: String,
    val Isha: String,
    val Maghrib: String,
    val Midnight: String,
    val Sunrise: String,
    val Sunset: String
)

