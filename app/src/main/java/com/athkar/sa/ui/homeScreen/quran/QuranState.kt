package com.athkar.sa.ui.homeScreen.quran

import androidx.compose.runtime.Immutable
import com.athkar.sa.db.entity.SurahDownloadEntity
import com.athkar.sa.quran.Surah
import com.athkar.sa.quran.quran_download.ProgressInfo
import com.athkar.sa.worker.utils.QuranDownloadStatus


@Immutable
data class QuranState(
    val katmahState:KhatmahUi? = null,
    val dialogStatus: DialogState = DialogState(),
    val showAlertDialog:Boolean = false ,
    val allSurah :List<Surah> = emptyList(),
    val surahDownloads:List<SurahDownloadEntity> = emptyList()
)

@Immutable
data class DialogState(
    val showDialogProgress: Boolean = false ,
    val progressStatusInfo: QuranDownloadStatus? = null ,
    val isExtractingFiles:Boolean = false
)
@Immutable
data class KhatmahUi(
    val currentVerse:String,
    val part:Int,
    val previousWard:Int,
    val nextWard:Int,
){
    var allWards:Int = previousWard + nextWard
    fun calculateProgress():Float =  previousWard / allWards.toFloat()
}