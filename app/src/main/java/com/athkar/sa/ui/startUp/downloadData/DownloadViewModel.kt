package com.athkar.sa.ui.startUp.downloadData

import androidx.lifecycle.*
import androidx.work.*
import com.athkar.sa.ConstantsWorker
import com.athkar.sa.repo.Repository
import com.athkar.sa.worker.FetchDataCalenderPrays
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class DownloadViewModel @Inject constructor(
    private val repository: Repository,
    private val workManager: WorkManager
) : ViewModel() {

    private val oneTimeRequestWorker = OneTimeWorkRequestBuilder<FetchDataCalenderPrays>()
        .setConstraints(Constraints().apply {
            requiredNetworkType = NetworkType.CONNECTED
        }).build()
    val workInfo = workManager.getWorkInfosForUniqueWorkLiveData(ConstantsWorker.DOWNLOAD_WORKER)
    fun enqueueWorker() {
        workManager.enqueueUniqueWork(
            ConstantsWorker.DOWNLOAD_WORKER,
            ExistingWorkPolicy.KEEP,
            oneTimeRequestWorker
        )
    }

    fun cancelWorker() {
        workManager.cancelUniqueWork(ConstantsWorker.DOWNLOAD_WORKER)
    }
}