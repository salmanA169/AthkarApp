package com.athkar.sa.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SurahDownloadEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val title:String,
    val surahPath:String,
    val readerId:Int,
    val surahId:Int,
    val moshaf:String
)
