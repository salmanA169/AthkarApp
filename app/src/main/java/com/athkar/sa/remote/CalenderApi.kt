package com.athkar.sa.remote

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CalenderApi {
    @GET("calendarByCity")
    suspend fun getCalenderByCity(
        @Query(value = "city") city: String,
        @Query("country") country: String,
        @Query("annual") annual :Boolean = true,
        @Query("method") method : Int = 4,
    ): Response<ResponseCalendar>
}