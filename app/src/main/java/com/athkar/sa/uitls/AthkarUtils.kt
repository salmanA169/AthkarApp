package com.athkar.sa.uitls

import android.graphics.Color
import com.athkar.sa.models.Athkar
import com.athkar.sa.models.AthkarCategory

fun Athkar.setColorAthkat():Int{
    return when(category){
        AthkarCategory.MORNING -> Color.parseColor("#8ECAE6")
        AthkarCategory.EVENING ->Color.parseColor("#E63946")
        AthkarCategory.MY_ATHKAR ->Color.parseColor("#0A9396")
        AthkarCategory.PRAY ->Color.parseColor("#CA6702")
        AthkarCategory.NAMES_GOD ->Color.parseColor("#800F2F")
        AthkarCategory.SLEEPING ->Color.parseColor("#B5179E")
        AthkarCategory.WAKE_UP ->Color.parseColor("#FFAFCC")
        AthkarCategory.AFTER_PRAY ->Color.parseColor("#0077B6")
    }
}