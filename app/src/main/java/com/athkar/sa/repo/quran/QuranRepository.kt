package com.athkar.sa.repo.quran

import com.athkar.sa.components.SurahItemsStatus
import com.athkar.sa.db.entity.SurahDownloadEntity
import com.athkar.sa.quran.Surah
import com.athkar.sa.quran.dto.QuranDataModelDto
import com.athkar.sa.worker.utils.QuranDownloadStatus
import kotlinx.coroutines.flow.Flow
import java.io.Closeable

interface QuranRepository {

    /**
     * @return file path downloaded
     */
    fun startDownload()
    fun observeDownloadStatus(): Flow<QuranDownloadStatus?>

    suspend fun checkQuranData(): Boolean
    fun getAllSurah(): List<Surah>

    fun getAllQuranPages(): Int
    fun getPageBySurahId(surahId: Int): Int
    fun getPagePath(pageNum: Int): String
    fun getSurahNameBySurahId(surahId: Int): String
    fun getQuranSurahInfo(page: Int): QuranSurahInfo
    fun getAllSurahPagesPath(): List<String>
    suspend fun getDarkModePage(): Boolean
    suspend fun updateDarkModePage(darkMode: Boolean)

    suspend fun getReaders(): Result<QuranDataModelDto>

    suspend fun getAudioFilesStatus(
        surahNames: List<String>,
        readerId: Int,
        moshaf: String
    ): Flow<List<SurahItemsStatus>>

    suspend fun deleteDownloadReaderFile(readerId: Int, moshaf: String, surahId: Int)
    fun cancelDownload(surahId: Int)

    suspend fun downloadAudio(uriDownload: String, readerId: Int, moshaf: String, surahId: Int,readerName:String)
    fun getCloseables(): List<Closeable>


    suspend fun upsertDownloadSurah(surahDownloadEntity: SurahDownloadEntity)
    fun getSurahDownloadFlow():Flow<List<SurahDownloadEntity>>
    suspend fun deleteSurahDownload(uri:String)
}

interface QuranRepoEvent{
    fun onShowError(message:String)
}
data class QuranSurahInfo(
    val surahName: String,
    val pagePath: String,
    val currentPage: String,
    val surahId: Int
)