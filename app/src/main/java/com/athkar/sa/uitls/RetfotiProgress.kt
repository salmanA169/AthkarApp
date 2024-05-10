package com.athkar.sa.uitls

import com.athkar.sa.retrofit.DownLoad
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import okio.use
import java.io.ByteArrayOutputStream

// TODO: fix issue that outMemory one solution is save btyes to direct file not return all bytes to memory
fun ResponseBody.progressResponse() =
    flow {
        emit(DownLoad.Loading)
        val size = contentLength()
        var buffer = ByteArray(8 * 1024)
        val tempBuffer = ByteArrayOutputStream()
        byteStream().use { input ->
            tempBuffer.use { tt ->
                var bytes = input.read(buffer)
                var bytesCopied = 0
                while (bytes >= 0) {
                    bytesCopied += bytes
                    emit(DownLoad.Progress((bytesCopied.toFloat() / size)))
                    bytes = input.read(buffer)
                    if (bytes != -1) {
                        tt.write(buffer, 0, bytes)
                    }
                }
            }
            emit(DownLoad.Finish(tempBuffer.toByteArray()))
        }
    }

fun formatTextToGetFileFromServer(surahId: String) =
    if (surahId.toInt() < 10) {
        "00$surahId"
    } else if (surahId.toInt() < 100) {
        "0$surahId"
    } else {
        surahId
    }
