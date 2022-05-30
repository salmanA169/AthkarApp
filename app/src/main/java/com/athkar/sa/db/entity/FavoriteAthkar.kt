package com.athkar.sa.db.entity

import com.athkar.sa.models.AthkarCategory

data class FavoriteAthkar(
    val nameAlthkr:String,
    val athkarCategory: AthkarCategory
)