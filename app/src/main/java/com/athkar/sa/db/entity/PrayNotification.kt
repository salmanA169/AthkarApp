package com.athkar.sa.db.entity

import com.athkar.sa.PrayUtil

data class PrayNotification(
    val fajer :Boolean,
    val sunRise :Boolean,
    val duhar :Boolean,
    val asar :Boolean,
    val maghrab :Boolean,
    val isha :Boolean,
){
}
