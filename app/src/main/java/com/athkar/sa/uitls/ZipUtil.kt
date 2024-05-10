package com.athkar.sa.uitls

import android.util.Log
import androidx.annotation.VisibleForTesting
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.zip.ZipFile


private const val BUFFER_SIZE = 512
private const val MAX_FILES = 12000 // Max number of files


@VisibleForTesting
var MAX_UNZIPPED_SIZE = 0x1f400000 // Max size of unzipped data, 500MB

fun unzipFile(
    zipFile: String?,
    destDirectory: String?,
): Boolean {
    return try {
        val file = File(zipFile)
        Log.d("unZipFiles","unzipping $zipFile, size: ${file.length()}")
        ZipFile(file, ZipFile.OPEN_READ).use { zip ->
            val numberOfFiles = zip.size()
            val entries = zip.entries()
            val canonicalPath = File(destDirectory).canonicalPath
            var total: Long = 0
            var processedFiles = 0
            while (entries.hasMoreElements()) {
                processedFiles++
                val entry = entries.nextElement()
                val currentEntryFile = File(destDirectory, entry.name)
                if (currentEntryFile.canonicalPath.startsWith(canonicalPath)) {
                    if (entry.isDirectory) {
                        if (!currentEntryFile.exists()) {
                            currentEntryFile.mkdirs()
                        }
                        continue
                    } else if (currentEntryFile.exists()) {
                        // delete files that already exist
                        currentEntryFile.delete()
                    }
                    zip.getInputStream(entry).use { `is` ->
                        FileOutputStream(currentEntryFile).use { ostream ->
                            var size: Int = 0
                            val buf =
                                ByteArray(BUFFER_SIZE)
                            while (total +BUFFER_SIZE <=MAX_UNZIPPED_SIZE && `is`.read(
                                    buf
                                ).also {
                                    size = it
                                } > 0
                            ) {
                                ostream.write(buf, 0, size)
                                total += size.toLong()
                            }
                        }
                    }
                    check(!(processedFiles >= MAX_FILES || total >=MAX_UNZIPPED_SIZE)) { "Invalid zip file." }

                } else {
                    throw IllegalStateException("Invalid zip file.")
                }
            }
        }
        true
    } catch (ioe: IOException) {
        Log.e("unZipfile", "Error unzipping file",ioe)
        false
    }
}