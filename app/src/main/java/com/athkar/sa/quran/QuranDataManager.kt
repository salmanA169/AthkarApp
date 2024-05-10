package com.athkar.sa.quran

import android.util.Log
import com.athkar.sa.quran.data.QuranDataSource
import com.athkar.sa.quran.quran_download.ProgressInfo
import com.athkar.sa.quran.quran_download.QuranDownloader
import com.athkar.sa.uitls.unzipFile
import kotlinx.coroutines.delay
import java.io.File
import javax.inject.Inject

class QuranDataManager @Inject constructor(
    private val quranDataSource: QuranDataSource,
    private val quranFile: QuranFileManager,
    private val quranDownloader: QuranDownloader,
    private val quranSettings: QuranSettings
) {

    private val quranInfo = QuranInfoImpl()

    suspend fun getDarkModePage() = quranSettings.getCurrentDarkMode()


    suspend fun saveFile(fileName: String, data: ByteArray, readerId: Int, moshaf: String):String {
        return quranFile.saveAudioFile(readerId, moshaf, fileName, data)
    }

    suspend fun deleteDownloadReaderFile(readerId: Int,moshaf: String,surahId: Int){
        quranFile.deleteDownloadSurahReaderFile(readerId, moshaf, surahId)
    }
    suspend fun getDownloadedReader(readerId: Int,moshaf: String):List<FileMetaData>{
        return quranFile.getDownloadedSurahByReaderMoshaf(readerId,moshaf)
    }
    suspend fun updateDarkMode(darkMode: Boolean) {
        quranSettings.updateDarkModePage(darkMode)
    }

    suspend fun startDownload(
        basePath: String,
        onProgressChanges: (ProgressInfo) -> Unit
    ) {
        val getDownloadZip = quranDownloader.downloadQuranPages(
            "v${quranDataSource.currentQuranVersion}",
            basePath,
            onProgressChanges
        )
        onProgressChanges(ProgressInfo("Extracting", 0, 0, true))
        // TODO: improve later to file quran utils
        unzipFile(getDownloadZip, basePath + File.separator + "quranPages")

        try {
            File(getDownloadZip).delete()
        } catch (e: Exception) {

        }
    }

    fun getAllSurahPagesPath() = quranFile.getAllPagesPath()
    fun getAllSurah(): List<Surah> {
        val allSurah = quranInfo.allSurah
        quranInfo.pageSurah.forEachIndexed { index, i ->
            allSurah[index].pageNumb = i
        }
        return allSurah
    }

    fun getSurahNameBySurahId(surahId:Int):String {
        val getAllSurah = getAllSurah()
        return getAllSurah.find { it.id == surahId }!!.name
    }
    fun getSurahPagePath(idSurah: Int): String {
        return getPagePath(quranInfo.pageSurah[idSurah - 1])
    }

    fun getAllQuranPages(): Int {
        return quranInfo.quranPages
    }

    fun getSurahNameByPage(page: Int): Surah {
        val allQuran = quranInfo.allSurah
        val quranPages = quranInfo.pageSurah
        var surah: Surah? = null

        for (i in 0..quranPages.size) {
            val getPage = quranPages[i]
            if (i == quranPages.size - 1) {
                surah = allQuran[i]
                break
            }
            val getNextPage = quranPages[i + 1]
            if (page == getPage) {
                surah = allQuran[i]
                break
            } else if (page < getNextPage) {
                surah = allQuran[i]
                break
            }
        }
        return surah!!
    }

    fun getSurahPageBySurahId(surahId: Int): Int {
        return quranInfo.pageSurah[surahId - 1]
    }

    fun getPagePath(page: Int): String {
        return quranFile.getPagePath(page)
    }

    suspend fun checkQuranData(): Boolean {
        val checkQuranPage = quranFile.checkQuranPages(quranInfo)
        return if (checkQuranPage.isSuccess) {
            checkQuranPage.getOrThrow()
        } else {
            false
        }
    }
}

data class Surah(
    val id: Int,
    val name: String,
    val verseNumber: Int,
    var pageNumb: Int = 0
)
