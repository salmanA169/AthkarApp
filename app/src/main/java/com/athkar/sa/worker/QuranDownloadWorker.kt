package com.athkar.sa.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.athkar.sa.quran.QuranDataManager
import com.athkar.sa.quran.quran_download.QuranDownloader
import com.athkar.sa.worker.utils.QuranWorkerUtil
import com.google.firebase.storage.FirebaseStorage
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class QuranDownloadWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val quranDataManager: QuranDataManager
) : CoroutineWorker(context, workerParameters) {


    override suspend fun doWork(): Result {
        quranDataManager.startDownload(applicationContext.filesDir.absolutePath) {
            setProgressAsync(
                workDataOf(
                    QuranWorkerUtil.QURAN_DOWNLOAD_PROGRESS to it.calculateProgress(),
                    QuranWorkerUtil.QURAN_DOWNLOAD_SIZE to it.totalSize,
                    QuranWorkerUtil.QURAN_DOWNLOAD_SIZE_DOWNLOADED to  it.downloadSize,
                    QuranWorkerUtil.QURAN_DOWNLOAD_EXTRACTING to it.extracting,
                    QuranWorkerUtil.QURAN_DOWNLOAD_LABEL to it.label
                )
            )
        }
        return Result.success()
    }
}
