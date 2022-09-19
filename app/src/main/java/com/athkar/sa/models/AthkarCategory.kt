package com.athkar.sa.models

import androidx.annotation.Keep

enum class AthkarCategory(val nameAthkar: String,val color : String) {
    MORNING("أذكار الصباح","#8ECAE6"),
    EVENING("أذكار المساء","#E63946"),
    PRAY("أذكار الصلاة","#CA6702"),
    SLEEPING("أذكار النوم","#B5179E"),
    WAKE_UP("أذكار الاستيقاظ من النوم","#FFAFCC"),
    AFTER_PRAY("أذكار بعد الصلاة","#0077B6"),
    FROM_QURAN("أدعية من القرأن","#999999"),
    FROM_PROPHET("من دعاء الرسول","#f7ce76"),
    RUQIA_QURAN("الرقية بالقرأن","#f19e70"),
    RUQIA_SUNNAH("الرقية بالسنة","#729799")
}

fun getAthkar(): List<AthkarCategory> {

   return AthkarCategory.values().toList()
}