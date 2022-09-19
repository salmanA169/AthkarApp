package com.athkar.sa.models

import android.os.Parcelable
import com.athkar.sa.db.entity.FavoriteAthkar
import kotlinx.parcelize.Parcelize

@Parcelize
data class FavoriteTracker(
    val id:Int,
    val nameAlthker:String,
    val category: AthkarCategory
) : Parcelable

fun FavoriteTracker.toFavoriteAlthker() = FavoriteAthkar(
    id,nameAlthker,category
)