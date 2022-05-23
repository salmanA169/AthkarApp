package com.athkar.sa.models

data class Athkar(val category: AthkarCategory)
enum class AthkarCategory(val nameAthkar: String) {
    MORNING("أذكار الصباح"),
    EVENING("أذكار المساء"),
    MY_ATHKAR("أذكاري"),
    PRAY("أذكار الصلاة"),
    NAMES_GOD("أسماء الله الحسنى"),
    SLEEPING("أذكر النوم"),
    WAKE_UP("أذكار الاستيقاظ من النوم"),
    AFTER_PRAY("أذكار بعد الصلاة");

}

fun getAthkar(): List<Athkar> {
   return AthkarCategory.values().map {
       Athkar(it)
   }
}