package com.athkar.sa.db.entity

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.athkar.sa.models.AthkarCategory
import kotlinx.parcelize.Parcelize
@Keep
@Entity
@Parcelize
data class Athkar(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val nameAlthker: String,
    val titleAlthker: String?,
    val category: AthkarCategory,
    val howTo: String?,
    val position: Int,
    @Embedded
    val hadithContent: HadithContent?,
    @Embedded
    val QuranContent: QuranContent?,
    val times: Int,
    val strength: String?
) : Parcelable {
    @Ignore
    fun getContentAlthker(): String {
        return buildString {
            if (QuranContent != null) {
                val quran = QuranContent
                append(quran.surah)
                appendLine()
                appendLine()
                append(quran.getAyah())
                appendLine()
                appendLine()
                append(strength)
            } else if (hadithContent != null) {
                val hadith = hadithContent
                val listHadith = hadithContent.listHadith
                if (listHadith != null) {
                    listHadith.forEach {
                        append(it)
                        appendLine()
                        appendLine()
                    }
                } else {
                    append(hadith.hadith)
                    appendLine()
                    append(strength)
                }
            }
        }
    }
}
@Keep
@Parcelize
data class HadithContent(val hadith: String?, val listHadith: List<String>?) : Parcelable

private const val BSMALLAH = "بسم الله الرحمن الرحيم"
private const val BSMALLAH1 = "أعوذ بالله من الشيطان الرجيم"
@Keep
@Parcelize
data class QuranContent(
    val surah: String,
    val surahName: String,
    val startOfAyah: Int,
    val endOfAyah: Int
) : Parcelable {

    @Ignore
    fun getBasmlah() = if (startOfAyah == 1) BSMALLAH else BSMALLAH1

    @Ignore
    fun getAyah() =
        if (startOfAyah == endOfAyah) "($startOfAyah / $surahName)" else if (endOfAyah > startOfAyah) "($startOfAyah - $endOfAyah / $surahName)" else null
}
