package com.athkar.sa.quran.quran_download

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.athkar.sa.retrofit.DownLoad
import com.athkar.sa.retrofit.ResponseBodyProgress
import com.athkar.sa.Constants
import com.athkar.sa.quran.QuranConstants
import com.athkar.sa.remote.QuranApi
import com.athkar.sa.uitls.getFileSize
import com.athkar.sa.uitls.progressResponse
import com.google.android.gms.tasks.Tasks
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.asDeferred
import kotlinx.coroutines.tasks.await
import okhttp3.OkHttpClient
import okio.use
import retrofit2.Retrofit
import retrofit2.await
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import javax.inject.Inject
import kotlin.random.Random
import kotlin.system.measureTimeMillis
import kotlin.time.Duration.Companion.milliseconds

class QuranDownloader @Inject constructor(
) {

    private val storage = Firebase.storage

    suspend fun downloadQuranPages(
        version: String,
        basePath: String,
        progress: (ProgressInfo) -> Unit
    ): String {

        val file = File(basePath, QuranConstants.DOWNLOAD_QURAN_PAGE_NAME)
        val getData =
            storage.reference.child(version).child(QuranConstants.DOWNLOAD_QURAN_PAGE_NAME)
                .getFile(file)
        val task = getData.addOnProgressListener {
            progress(ProgressInfo("Downloading...", it.totalByteCount, it.bytesTransferred,it.totalByteCount == it.bytesTransferred))
        }
        getData.await()
        return file.absolutePath
    }
}

data class ProgressInfo(
    val label: String,
    val totalSize: Long,
    val downloadSize: Long,
    val extracting: Boolean = false
) {
    fun calculateProgress() = downloadSize.toFloat() / totalSize.toFloat()
}