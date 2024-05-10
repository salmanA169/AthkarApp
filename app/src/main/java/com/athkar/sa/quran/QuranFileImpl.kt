package com.athkar.sa.quran

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileDescriptor
import java.io.FileOutputStream
import javax.inject.Inject

class QuranFileImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val quranSettings: QuranSettings
) : QuranFileManager {

    private val TAG = javaClass.simpleName
    private val basePath = context.filesDir
    private val quranFolder = "quranPages"
    private val folderName = File.separator.plus(quranFolder)
    private val audioFileDirectory = "audioDownload"
    private val folderAudioName = File.separator.plus(audioFileDirectory)
    override suspend fun checkVersions(version: Int): Result<Boolean> {
        return if (quranSettings.getCurrentVersion() != version) {
            updateNewQuranVersion(context, version)
        } else {
            Result.success(true)
        }
    }

    private fun formatAudioDownloadedReaderAndMoshat(readerId: Int,moshaf: String):String{
        return folderAudioName + File.separator + readerId.toString() + File.separator + moshaf
    }
    private fun formatAudioFolderDownload(readerId: Int, moshaf: String, fileName: String): String {
        makeDirForReaderId(readerId.toString(),moshaf)
        val formatFile = formatAudioDownloadedReaderAndMoshat(readerId,moshaf)
        return  formatFile + File.separator + fileName
    }

    private fun createNewFolderForAudioDownload(): File {
        return File(basePath, folderAudioName).apply {
            if (!exists())
                mkdir()
        }
    }

    private fun makeDirForReaderId(readerId: String,moshaf:String) {
        val readerFile = File(createNewFolderForAudioDownload(), readerId).apply {
            if (!exists()) {
                mkdir()
            }

        }
        val moshafDir =  File(readerFile,moshaf)
        if (!moshafDir.exists()){
            moshafDir.mkdir()
        }
    }

    private suspend fun updateNewQuranVersion(context: Context, newVersion: Int): Result<Boolean> {
        // first check if file is exist
        return Result.success(checkVersionExist(newVersion))
    }

    override suspend fun deleteDownloadSurahReaderFile(
        readerId: Int,
        moshaf: String,
        surahId: Int
    ) {
        val formatFile = formatAudioFolderDownload(readerId,moshaf,surahId.toString()).plus(".mp3")
        File(basePath,formatFile).delete()
    }

    override suspend fun getDownloadedSurahByReaderMoshaf(readerId: Int, moshaf: String): List<FileMetaData> {
        val file = File(basePath,formatAudioDownloadedReaderAndMoshat(readerId ,moshaf))
        return file.listFiles()?.map {
            FileMetaData(
                it.absolutePath,
                it.nameWithoutExtension
            )
        }?: emptyList()
    }

    override suspend fun saveAudioFile(
        readerId: Int,
        moshaf: String,
        fileName: String,
        fileData: ByteArray
    ):String {
        val destPath = formatAudioFolderDownload(readerId, moshaf, fileName).plus(".mp3")
        return try {
            File(basePath, destPath).apply {
                writeBytes(fileData)
            }.absolutePath
        } catch (e: Exception) {
            Log.e(TAG, "saveAudioFile: unKnown Error", e)
            ""
        }
    }

    private fun checkVersionExist(version: Int): Boolean {
        return true
    }

    private fun checkQuranFolder(): Boolean {
        val quranFolder = File(basePath, folderName)
        return quranFolder.exists() && quranFolder.isDirectory
    }

    private fun getQuranFolder(): File {
        return File(basePath, folderName).apply {
            if (!exists()) {
                mkdir()
            }
        }
    }

    override fun getAllPagesPath(): List<String> {
        return getQuranFolder().listFiles().map {
            it.absolutePath
        }
    }

    override fun getPagePath(page: Int): String {
        val getQuranFolder = getQuranFolder()
        return getQuranFolder.listFiles().find {
            it.name.substringAfter("page").removeSuffix(".png").toInt() == page
        }!!.absolutePath
    }

    override suspend fun checkQuranPages(quranInfo: QuranInfo): Result<Boolean> {
        val getQuranFolder = getQuranFolder()
        if (!getQuranFolder.exists() || !getQuranFolder.isDirectory) {
            return Result.failure(Exception("There is no folder for quran page"))
        }
        val listFiles = getQuranFolder.listFiles()
        listFiles.sort()
        if (listFiles.isEmpty()) {
            return Result.failure(Exception("Empty files"))
        }
        val quranPages = (1..quranInfo.quranPages - 1).toList()
        quranPages.forEachIndexed { index, i ->
            val supPage =
                listFiles.getOrNull(index)?.name?.substringAfter("page")?.removeSuffix(".png")
            if (supPage == null || i != supPage.toInt()) {
                return Result.failure(Exception("There is missing page for $supPage"))
            }
        }
        return Result.success(true)
    }

    override suspend fun deleteAllPages() {
    }

    override suspend fun savePages(filePath: String, bitmap: Bitmap) {
        val quranFolder = getQuranFolder()
        val file = File(quranFolder, filePath)
        val output = FileOutputStream(file)
        try {
            output.use {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
            }
        } catch (e: Exception) {
            Log.e("QuranFileImpl", "savePages: called with error save Page", e)
        }
    }
}