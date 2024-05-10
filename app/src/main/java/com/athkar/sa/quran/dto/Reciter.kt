package com.athkar.sa.quran.dto

import androidx.annotation.Keep

@Keep
data class Reciter(
    val id: Int,
    val letter: String,
    val moshaf: List<Moshaf>,
    val name: String
)