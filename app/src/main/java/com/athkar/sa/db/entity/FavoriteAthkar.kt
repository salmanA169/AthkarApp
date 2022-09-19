package com.athkar.sa.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.athkar.sa.models.AthkarCategory
import com.athkar.sa.models.FavoriteTracker

@Entity
data class FavoriteAthkar(
    @PrimaryKey(autoGenerate = true)
    val id : Int,
    val nameAlthkr:String,
    val athkarCategory: AthkarCategory,
)

fun FavoriteAthkar.toFavoriteTracker() = FavoriteTracker(
    id,nameAlthkr,athkarCategory
)