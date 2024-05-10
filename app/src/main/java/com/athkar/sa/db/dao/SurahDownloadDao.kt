package com.athkar.sa.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.athkar.sa.db.entity.SurahDownloadEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SurahDownloadDao {
    @Query("SELECT * FROM SurahDownloadEntity")
    fun getSurahDownloads():Flow<List<SurahDownloadEntity>>

    @Upsert
    suspend fun upsertSurahDownload(surahDownloadEntity: SurahDownloadEntity)

    @Query("DELETE FROM SurahDownloadEntity WHERE surahPath = :surahPath")
    suspend fun deleteSurahDownload(surahPath:String)
}