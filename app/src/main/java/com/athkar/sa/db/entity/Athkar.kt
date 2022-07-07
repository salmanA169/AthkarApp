package com.athkar.sa.db.entity

import com.athkar.sa.models.AthkarCategory

// TODO: fix later
data class Athkar(
    val nameAlthker: String,
    val category: AthkarCategory,
    val order: Int
)