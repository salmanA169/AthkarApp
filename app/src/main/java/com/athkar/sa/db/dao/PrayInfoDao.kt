package com.athkar.sa.db.dao

import androidx.room.*
import com.athkar.sa.db.entity.PrayInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface PrayInfoDao {

    @Query("SELECT * FROM PrayInfo ")
    fun getPrayInfo(): Flow<List<PrayInfo>>

    @Query("SELECT * FROM PrayInfo WHERE day =:day AND month =:month ")
    suspend fun getTodayPrayInfo(day:Int,month:Int):PrayInfo?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPrayInfo(prayInfo: PrayInfo)

    @Delete
    suspend fun removePrayInfo(prayInfo: PrayInfo)

    @Query("DELETE FROM prayinfo")
    suspend fun deleteAllTable()
}