package com.athkar.sa.uitls

import android.graphics.Color
import java.text.DecimalFormat

fun getFileSize(size: Long): String {
    if (size <= 0) return "0"
    val units = arrayOf("B", "KB", "MB", "GB", "TB")
    val digitGroups = (Math.log10(size.toDouble()) / Math.log10(1024.0)).toInt()
    return DecimalFormat("#,##0.#").format(
        size / Math.pow(
            1024.0,
            digitGroups.toDouble()
        )
    ) + " " + units[digitGroups]
}
fun formatSurahWithSurahName(readerName:String,surahName:String) = "$readerName - $surahName"

fun String.parseColor() = Color.parseColor(this)
fun String.formatTimePray() = this.substring(0..4)

fun Int.formatMonthToString():String =  when(this){
    1->{
        "محرم"
    }
    2->{
        "صفر"
    }
    3->{
        "ربيع اول"
    }
    4->{
        "ربيع الثاني"
    }
    5->{
        "جمادى الاول"
    }
    6->{
        "جمادى الثاني"
    }
    7->{
        "رجب"
    }
    8->{
        "شعبان"
    }
    9->{
        "رمضان"
    }
    10->{
        "شوال"
    }
    11->{
        "ذو القعدة"
    }

    12->{
        "ذو الحجة"
    }
    else -> throw Exception("Month $this can not format to hijra date ")
}