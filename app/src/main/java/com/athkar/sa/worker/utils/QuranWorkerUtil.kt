package com.athkar.sa.worker.utils

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.await
import com.athkar.sa.uitls.getFileSize
import com.athkar.sa.worker.QuranDownloadWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class QuranWorkerUtil  @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object{
        const val QURAN_DOWNLOAD_WORKER = "quran-worker"
        const val QURAN_DOWNLOAD_PROGRESS = "quran-progress"
        const val QURAN_DOWNLOAD_SIZE = "quran-size"
        const val QURAN_DOWNLOAD_SIZE_DOWNLOADED = "quran-downloaded-size"
        const val QURAN_DOWNLOAD_EXTRACTING = "quran-extracting"
        const val QURAN_DOWNLOAD_LABEL = "quran-download-label"
    }
    private val workManager = WorkManager.getInstance(context)
    private val requestWorker = OneTimeWorkRequestBuilder<QuranDownloadWorker>().setConstraints(
        Constraints(NetworkType.CONNECTED)
    ).build()
    private val workerFlow = workManager.getWorkInfosForUniqueWorkFlow(QURAN_DOWNLOAD_WORKER)
    fun enqueueQuranWork() {
        workManager.enqueueUniqueWork(QURAN_DOWNLOAD_WORKER,ExistingWorkPolicy.KEEP,requestWorker)
    }
    fun observeProgress():Flow<QuranDownloadStatus?>{
        return workerFlow.map {
            it.getOrNull(0)
        }.map {
            if (it == null) return@map null
            val getProgress = it.progress.getFloat(QURAN_DOWNLOAD_PROGRESS,0f)
            val getSize = it.progress.getLong(QURAN_DOWNLOAD_SIZE,0)
            val getDownloaded = it.progress.getLong(QURAN_DOWNLOAD_SIZE_DOWNLOADED,0)
            val extracting = it.progress.getBoolean(QURAN_DOWNLOAD_EXTRACTING , false )
            val getLabel = it.progress.getString(QURAN_DOWNLOAD_LABEL)
            QuranDownloadStatus(getLabel?:"",it.state == WorkInfo.State.RUNNING,getProgress,
                getFileSize(getSize), getFileSize(getDownloaded),extracting
            )
        }
    }
}
data class QuranDownloadStatus(
    val label:String,
    val isStartDownload :Boolean,
    val progress:Float,
    val fileSize :String,
    val downloadedSize:String,
    val extracting:Boolean = false
)