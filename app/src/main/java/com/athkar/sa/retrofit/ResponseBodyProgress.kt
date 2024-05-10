package com.athkar.sa.retrofit

import android.util.Log
import com.athkar.sa.uitls.getFileSize
import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.Buffer
import okio.BufferedSource
import okio.ForwardingSource
import okio.Source
import okio.buffer
import java.io.File
import java.io.FileOutputStream

sealed class DownLoad {
    data object None: DownLoad()
    data object Loading: DownLoad()
    data class Progress(val progress: Float) : DownLoad()
    data class Finish(val fileData:ByteArray) : DownLoad()
}

class ResponseBodyProgress(
    val responseBody: ResponseBody,
    val onUpdate: (DownLoad) -> Unit,
    val basePath: String
) : ResponseBody() {

    private val file = File(basePath, "test.mp3")

    override fun contentLength(): Long {
        return responseBody.contentLength()
    }

    override fun contentType(): MediaType? {
        return responseBody.contentType()
    }

    override fun source(): BufferedSource {
        return SourceData(responseBody.source(), onUpdate, responseBody, file).buffer()
    }
}

class SourceData(
    private val source: Source,
    private val onChange: (DownLoad) -> Unit,
    private val responseBody: ResponseBody,
    private val file: File
) : ForwardingSource(source) {
    private var totalByteReads = 0L
    override fun read(sink: Buffer, byteCount: Long): Long {
        val bytesRead = super.read(sink, byteCount)
//        sink.writeTo(FileOutputStream(file))
        totalByteReads += if (bytesRead != -1L) bytesRead else 0
        val percent =
            if (bytesRead == -1L) 100f else totalByteReads.toFloat() / responseBody.contentLength()
                .toFloat() * 100
        onChange(DownLoad.Progress(percent))

        return bytesRead
    }
}