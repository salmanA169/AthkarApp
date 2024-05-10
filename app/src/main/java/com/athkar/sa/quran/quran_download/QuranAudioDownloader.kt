package com.athkar.sa.quran.quran_download

import android.util.Log
import com.athkar.sa.coroutine.DispatcherProviderImpl
import com.athkar.sa.di.QuranAudioApiProvider
import com.athkar.sa.quran.FileMetaData
import com.athkar.sa.quran.QuranInfoImpl
import com.athkar.sa.remote.QuranApi
import com.athkar.sa.retrofit.DownLoad
import com.athkar.sa.uitls.progressResponse
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.Closeable
import javax.inject.Inject

class QuranAudioDownloader @Inject constructor(
    @QuranAudioApiProvider private val quranApi: QuranApi,
) : QuranAudioFilesStatus {

    private val TAG: String = javaClass.simpleName
    var quranAudioEvent: QuranAudioEvent? = null
    private val quranInfo = QuranInfoImpl()

    private val dispatcherProvider = DispatcherProviderImpl()
    private val flow = MutableStateFlow<List<SurahReaderDownloadStatus>>(listOf())
    private val scope = CoroutineScope(dispatcherProvider.io + SupervisorJob())
    private val downloads = LinkedHashMap<String, Job>()
    private var currentJob: Job? = null

    override fun getSurahItemsByReaderIdAndMoshaf(
        readerId: Int,
        moshaf: String,
        surahList: List<String>,
        surahDownloaded: List<FileMetaData>
    ): Flow<List<SurahReaderDownloadStatus>> {
        getSurah(readerId, moshaf, surahList, surahDownloaded)
        return flow
    }

    private fun getSurah(
        readerId: Int,
        moshaf: String,
        surahList: List<String>,
        surahDownloaded: List<FileMetaData>
    ) {
        currentJob?.cancel()
        cancelAllDownloads()
        downloads.clear()
        currentJob = scope.launch(dispatcherProvider.io) {
            val list = mutableListOf<SurahReaderDownloadStatus>()
            surahList.forEach { surahListStringId ->
                val getSurah = quranInfo.allSurah.find { it.id == surahListStringId.toInt() }
                val getDownloadedSurah =
                    surahDownloaded.find { getSurah?.id.toString() == it.shortName }
                // set id for now surah id
                list.add(
                    SurahReaderDownloadStatus(
                        getSurah!!.id,
                        readerId,
                        moshaf,
                        getSurah.id,
                        getSurah.name,
                        DownLoad.None,
                        getDownloadedSurah?.absolutePath
                    )
                )
            }
            flow.value = list.toList()
        }
    }

    override fun cancel(surahId: Int) {
        try {
            downloads[surahId.toString()]?.cancel()
            resetSurahItemDownloadStats(surahId, DownLoad.None)
        } catch (e: Exception) {
            Log.e(TAG, "cancel: called error", e)
        }
    }

    private fun cancelAllDownloads() {
        downloads.forEach {
            it.value.cancel()
            resetSurahItemDownloadStats(it.key.toInt(), DownLoad.None)
        }
    }

    fun updateSavedFilePath(surahId: Int, path: String) {
        val getList = flow.value.toMutableList()
        val getIndex = getList.indexOfFirst { it.surahId == surahId }
        val getOldValue = getList.find { it.surahId == surahId }!!
        val newValue = getOldValue.copy(filePath = path, flowProgress = DownLoad.None)
        getList[getIndex] = newValue
        flow.value = getList.toList()
    }
    fun updateDeletedFile(surahId: Int) {
        val getList = flow.value.toMutableList()
        if (getList.isEmpty()) {
            return
        }
        val getIndex = getList.indexOfFirst { it.surahId == surahId }
        val getOldValue = getList.find { it.surahId == surahId }!!
        val newValue = getOldValue.copy(filePath = null)
        getList[getIndex] = newValue
        flow.value = getList.toList()
    }

    private fun resetSurahItemDownloadStats(surahId: Int, downLoad: DownLoad) {
        try {
            val getList = flow.value.toMutableList()
            val getIndex = getList.indexOfFirst { it.surahId == surahId }
            val getOldValue = getList.find { it.surahId == surahId }!!
            val newValue = getOldValue.copy(flowProgress = downLoad)
            getList[getIndex] = newValue
            flow.value = getList.toList()
        } catch (e: Exception) {
            Log.e(TAG, "resetSurahItemDownloadStats", e)
        }
    }

    override fun close() {
        currentJob?.cancel()
        cancelAllDownloads()
    }

    /**
     * for test : https://server6.mp3quran.net/akdr/002.mp3
     */
    private suspend fun download(uriDownload: String) =
        quranApi.getDownload(uriDownload)

    override suspend fun downloadAudio(
        uriDownload: String,
        readerId: Int,
        moshaf: String,
        surahId: Int,
        readerName: String
    ) {
        downloads[surahId.toString()] =
            scope.launch(dispatcherProvider.io + CoroutineExceptionHandler { coroutineContext, throwable ->
               quranAudioEvent?.onDownloadError("Can not download surah ${quranInfo.allSurah[surahId-1].name}")
            }) {
                download(uriDownload).progressResponse().collect {
                    val getList = flow.value.toMutableList()
                    val getIndex = getList.indexOfFirst { it.surahId == surahId }
                    val getIndexCurrentSurah = getList[getIndex]
                    val updatedValue = getIndexCurrentSurah.copy(flowProgress = it)
                    getList.set(getIndex, updatedValue)
                    flow.update {
                        getList.toList()
                    }

                    if (it is DownLoad.Finish) {
                        try {
                            quranAudioEvent?.onDownloadFinish(
                                readerId,
                                moshaf,
                                surahId.toString(),
                                it.fileData,
                                surahId, readerName
                            )
                            downloads[surahId.toString()]?.cancel()
                        } catch (e: Exception) {

                        }
                    }
                }
            }
    }
}

/**
 * val uri = uriDownLoad.toUri()
 *                     quranFile.saveAudioFile(readerId, moshaf,uri.lastPathSegment!!, it.fileData)
 */

interface QuranAudioEvent {
    suspend fun onDownloadFinish(
        readerId: Int,
        moshaf: String,
        fileName: String,
        fileData: ByteArray,
        surahId: Int,
        readerName: String
    )

    fun onDownloadError(message:String)
}

interface QuranAudioFilesStatus : Closeable {

    fun getSurahItemsByReaderIdAndMoshaf(
        readerId: Int,
        moshaf: String,
        surahList: List<String>,
        surahDownloaded: List<FileMetaData>
    ): Flow<List<SurahReaderDownloadStatus>>

    suspend fun downloadAudio(
        uriDownload: String,
        readerId: Int,
        moshaf: String,
        surahId: Int,
        readerName: String
    )

    fun cancel(surahId: Int)
}

data class SurahReaderDownloadStatus(
    val id: Int,
    val readerId: Int,
    val moshaf: String,
    val surahId: Int,
    val surahName: String,
    val flowProgress: DownLoad,
    val filePath: String? = null,
)