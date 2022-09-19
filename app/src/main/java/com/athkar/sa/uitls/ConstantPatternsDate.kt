package com.athkar.sa.uitls

import java.time.format.DateTimeFormatter
import java.util.*

object ConstantPatternsDate {
    val hijrahPattern = DateTimeFormatter.ofPattern("dd-MM-yyyy",Locale("ar", "sa"))
    val todayPattern = DateTimeFormatter.ofPattern("EEEE", Locale("ar", "sa"))
    val prayTimePattern = DateTimeFormatter.ofPattern("hh:mm a",Locale("ar", "sa"))
    val prayTimePattern1 = DateTimeFormatter.ofPattern("hh:mm",Locale("ar", "sa"))
}