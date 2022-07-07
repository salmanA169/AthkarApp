package com.athkar.sa.db.dao

import androidx.room.*
import com.athkar.sa.db.entity.PrayInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface PrayInfoDao {

    @Query("SELECT * FROM PrayInfo ")
    fun getPrayInfo(): Flow<List<PrayInfo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPrayInfo(prayInfo: PrayInfo)

    @Delete
    suspend fun removePrayInfo(prayInfo: PrayInfo)
}