package com.athkar.sa.models


enum class AthkarCategory(val nameAthkar: String) {
    MORNING("أذكار الصباح"),
    EVENING("أذكار المساء"),
    MY_ATHKAR("أذكاري"),
    PRAY("أذكار الصلاة"),
    NAMES_GOD("أسماء الله الحسنى"),
    SLEEPING("أذكار النوم"),
    WAKE_UP("أذكار الاستيقاظ من النوم"),
    AFTER_PRAY("أذكار بعد الصلاة");
}

fun getAthkar(): List<AthkarCategory> {
   return AthkarCategory.values().toList()
}