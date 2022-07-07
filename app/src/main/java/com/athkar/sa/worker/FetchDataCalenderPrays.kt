package com.athkar.sa.worker

import android.app.DownloadManager
import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.impl.model.Dependency
import androidx.work.workDataOf
import com.athkar.sa.remote.CalenderApi
import com.athkar.sa.remote.getAnnualTimes
import com.athkar.sa.repo.Repository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.delay
import java.sql.Timestamp
import java.time.LocalDateTime

@HiltWorker
class FetchDataCalenderPrays @AssistedInject constructor(
    @Assisted context:Context,
    @Assisted workerParameters: WorkerParameters,
    private val calenderApi:CalenderApi,
    private val repository: Repository
):CoroutineWorker(context,workerParameters) {

    override suspend fun doWork(): Result {

        return Result.failure()
    }
}