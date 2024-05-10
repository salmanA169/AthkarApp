package com.athkar.sa.quran.dto

import androidx.annotation.Keep

@Keep
data class Moshaf(
    val id: Int,
    val moshaf_type: Int,
    val name: String,
    val server: String,
    val surah_list: String,
    val surah_total: Int
)