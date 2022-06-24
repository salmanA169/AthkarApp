package com.athkar.sa.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CounterAlthker(
    @PrimaryKey(autoGenerate = false)
    val name:String,
    var count:Int
)
