package com.athkar.sa.remote

import com.athkar.sa.quran.dto.QuranDataModelDto
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

interface QuranApi {

    @GET("reciters")
    suspend fun getQuranRedder(): Response<QuranDataModelDto>

    @Streaming
    @GET
    suspend fun getDownload(@Url name: String): ResponseBody
}