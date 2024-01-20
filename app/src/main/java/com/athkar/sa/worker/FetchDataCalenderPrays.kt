package com.athkar.sa.worker

import android.content.Context
import android.location.Geocoder
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.athkar.sa.Constants
import com.athkar.sa.ConstantsWorker
import com.athkar.sa.db.entity.getDefaultPrayNotification
import com.athkar.sa.remote.CalenderApi
import com.athkar.sa.remote.getAnnualTimes
import com.athkar.sa.remote.toPrayInfo
import com.athkar.sa.repo.Repository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.tasks.CancellationTokenSource
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.time.Year
import java.util.*

@HiltWorker
class FetchDataCalenderPrays @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val locationService: FusedLocationProviderClient,
    private val calenderApi: CalenderApi,
    private val repository: Repository
) : CoroutineWorker(context, workerParameters) {


    private val TAG: String = javaClass.simpleName


    override suspend fun doWork(): Result {
        val tokenSrouce = CancellationTokenSource()
        return try {
            withContext(Dispatchers.IO) {
                val shouldRefresh =
                    inputData.getBoolean(ConstantsWorker.DOWNLOAD_DATA_WORKER_KEY, false)
                if (shouldRefresh) {
                    repository.deleteAllTablePrayInfo()
                }
                val getLocation =
                    locationService.getCurrentLocation(
                        LocationRequest.PRIORITY_HIGH_ACCURACY,
                        tokenSrouce.token
                    ).await()
                val geocoder = Geocoder(applicationContext, Locale.ENGLISH).getFromLocation(
                    getLocation.latitude,
                    getLocation.longitude,
                    1
                )

                val fetchPrayInfo = calenderApi.getCalenderByCity(
                    geocoder!!.first().locality,
                    geocoder.first().countryName,
                    year = Year.now().value
                ).body()!!

                fetchPrayInfo.data.getAnnualTimes().forEach {
                    repository.addPrayInfo(it.toPrayInfo(geocoder.first().locality))
                }
                val getOldNotification = repository.getPrayNotificationById()
                if (getOldNotification == null) {
                    repository.addPrayNotification(getDefaultPrayNotification())
                }
            }
            Result.success()
        } catch (e: Exception) {
            Log.e("FetchDataCalendarPray", "doWork: called", e)
            Result.failure()
        } finally {
            tokenSrouce.cancel()
        }
    }
}