package com.athkar.sa.remote

import com.google.gson.annotations.SerializedName
import retrofit2.http.Field
data class ResponseCalendar(
    @SerializedName("data")
    val data : CalenderDto
)
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

fun CalenderDto.getAnnualTimes() = mapOf<Int,List<TimingPrayDto>>(
    1 to january,
    2 to february,
    3 to march,
    4 to april,
    5 to may,
    6 to june,
    7 to july,
    8 to august,
    9 to september,
    10 to october,
    11 to november,
    12 to december,
)
data class TimingPrayDto(
    val timings :PrayInfoDto

)
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

