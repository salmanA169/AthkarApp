package com.athkar.sa.quran

import android.graphics.Bitmap
import com.athkar.sa.ui.homeScreen.quran.quran_page.SurahItem

interface QuranFileManager {

    suspend fun checkVersions(version:Int):Result<Boolean>
    suspend fun checkQuranPages(quranInfo: QuranInfo):Result<Boolean>
    suspend fun deleteAllPages()
    suspend fun savePages(filePath:String,bitmap: Bitmap)

    fun getPagePath(page:Int):String
    fun getAllPagesPath():List<String >

//    suspend fun getDownloadedFileByReaderId(readerId:Int,moshafName:String):List<SurahFile>
//

    /**
     * @return file saved path
     */
    suspend fun saveAudioFile(readerId:Int,moshaf:String,fileName:String,fileData:ByteArray):String

    suspend fun getDownloadedSurahByReaderMoshaf(readerId: Int,moshaf:String):List<FileMetaData>
    suspend fun deleteDownloadSurahReaderFile(readerId: Int,moshaf: String,surahId:Int)
}

data class FileMetaData(
    val absolutePath: String,
    val shortName:String
)