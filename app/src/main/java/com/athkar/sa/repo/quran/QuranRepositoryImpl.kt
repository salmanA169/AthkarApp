package com.athkar.sa.repo.quran

import android.net.ConnectivityManager
import com.athkar.sa.components.SurahItemsStatus
import com.athkar.sa.db.AthkarDatabase
import com.athkar.sa.db.entity.SurahDownloadEntity
import com.athkar.sa.di.QuranAudioApiProvider
import com.athkar.sa.quran.QuranDataManager
import com.athkar.sa.quran.Surah
import com.athkar.sa.quran.dto.QuranDataModelDto
import com.athkar.sa.quran.quran_download.QuranAudioDownloader
import com.athkar.sa.quran.quran_download.QuranAudioEvent
import com.athkar.sa.remote.QuranApi
import com.athkar.sa.uitls.hasNetwork
import com.athkar.sa.worker.utils.QuranDownloadStatus
import com.athkar.sa.worker.utils.QuranWorkerUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.Closeable
import javax.inject.Inject

class QuranRepositoryImpl @Inject constructor(
    private val quranDownloadWorker: QuranWorkerUtil,
    private val quranDataManager: QuranDataManager,
    @QuranAudioApiProvider private val quranApi: QuranApi,
    private val athkarDatabase: AthkarDatabase,
    private val connectivityManager: ConnectivityManager
) : QuranRepository, QuranAudioEvent {

    private val surahDownloadDao = athkarDatabase.surahDownloadEntity
    private val TAG = javaClass.simpleName
    private val quranAudioDownloader by lazy {
        QuranAudioDownloader(quranApi).apply {
            quranAudioEvent = this@QuranRepositoryImpl
        }
    }
    var quranRepEvent:QuranRepoEvent? = null
    override fun onDownloadError(message: String) {
        quranRepEvent?.onShowError(message)
    }

    override suspend fun upsertDownloadSurah(surahDownloadEntity: SurahDownloadEntity) {
        surahDownloadDao.upsertSurahDownload(surahDownloadEntity)
    }

    override fun getSurahDownloadFlow(): Flow<List<SurahDownloadEntity>> {
        return surahDownloadDao.getSurahDownloads()
    }

    override fun startDownload() {
        quranDownloadWorker.enqueueQuranWork()
    }

    override suspend fun downloadAudio(
        uriDownload: String,
        readerId: Int,
        moshaf: String,
        surahId: Int, readerName: String
    ) {
        quranAudioDownloader.downloadAudio(uriDownload, readerId, moshaf, surahId, readerName)
    }

    override fun cancelDownload(surahId: Int) {
        quranAudioDownloader.cancel(surahId)
    }

    override suspend fun deleteSurahDownload(uri: String) {
        surahDownloadDao.deleteSurahDownload(uri)
    }

    override suspend fun onDownloadFinish(
        readerId: Int,
        moshaf: String,
        fileName: String,
        fileData: ByteArray,
        surahId: Int,
        readerName: String
    ) {
        val path = quranDataManager.saveFile(fileName, fileData, readerId, moshaf)
        quranAudioDownloader.updateSavedFilePath(surahId, path)
        val getSurahName = quranDataManager.getSurahNameBySurahId(surahId)
        athkarDatabase.surahDownloadEntity.upsertSurahDownload(
            SurahDownloadEntity(
                0,
                readerName.plus(" | $getSurahName"),
                path,
                readerId, surahId, moshaf
            )
        )
    }

    override fun getCloseables(): List<Closeable> {
        return listOf(quranAudioDownloader)
    }

    override suspend fun deleteDownloadReaderFile(readerId: Int, moshaf: String, surahId: Int) {
        quranDataManager.deleteDownloadReaderFile(readerId, moshaf, surahId)
        quranAudioDownloader.updateDeletedFile(surahId)
    }

    override suspend fun getAudioFilesStatus(
        surahNames: List<String>,
        readerId: Int,
        moshaf: String,
    ): Flow<List<SurahItemsStatus>> {
        val getDownloadedReder = quranDataManager.getDownloadedReader(readerId, moshaf)
        return quranAudioDownloader.getSurahItemsByReaderIdAndMoshaf(
            readerId,
            moshaf,
            surahNames,
            getDownloadedReder
        ).map { list ->
            list.map {
                SurahItemsStatus(
                    "$readerId - $moshaf - ${it.surahId}",
                    it.readerId,
                    it.surahName,
                    it.surahId,
                    it.filePath,
                    false,
                    it.flowProgress
                )
            }
        }
    }


    override suspend fun getReaders(): Result<QuranDataModelDto> {
        try {
            if (!connectivityManager.hasNetwork()){
                return Result.failure(Exception("تأكد من أتصالك بالانترنت"))
            }
            val getResult = quranApi.getQuranRedder().body()!!
            return Result.success(getResult)
        }catch (e:Exception){
            return Result.failure(e)
        }
    }

    override suspend fun getDarkModePage(): Boolean {
        return quranDataManager.getDarkModePage()
    }

    override suspend fun updateDarkModePage(darkMode: Boolean) {
        quranDataManager.updateDarkMode(darkMode)
    }

    override fun observeDownloadStatus(): Flow<QuranDownloadStatus?> {
        return quranDownloadWorker.observeProgress()
    }

    override suspend fun checkQuranData(): Boolean {
        return quranDataManager.checkQuranData()
    }

    override fun getAllQuranPages(): Int {
        return quranDataManager.getAllQuranPages()
    }

    override fun getPageBySurahId(surahId: Int): Int {
        return quranDataManager.getSurahPageBySurahId(surahId)
    }

    override fun getPagePath(pageNum: Int): String {
        return quranDataManager.getPagePath(pageNum)
    }

    override fun getSurahNameBySurahId(surahId: Int): String {
        return quranDataManager.getSurahNameBySurahId(surahId)
    }

    override fun getAllSurahPagesPath(): List<String> {
        return quranDataManager.getAllSurahPagesPath().sorted()
    }

    override fun getAllSurah(): List<Surah> {
        return quranDataManager.getAllSurah()
    }

    override fun getQuranSurahInfo(page: Int): QuranSurahInfo {
        val getSurah = quranDataManager.getSurahNameByPage(page)
        val getPagePath = getPagePath(page)
        return QuranSurahInfo(getSurah.name, getPagePath, page.toString(), getSurah.id)
    }


}