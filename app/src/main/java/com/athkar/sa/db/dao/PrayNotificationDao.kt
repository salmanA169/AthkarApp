package com.athkar.sa.db.dao

import androidx.room.*
import com.athkar.sa.db.entity.PRAY_NOTIFICATION_ID
import com.athkar.sa.db.entity.PrayNotification
import kotlinx.coroutines.flow.Flow

@Dao
interface PrayNotificationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPrayNotification(prayNotification: PrayNotification)

    @Update
    suspend fun updatePrayNotification(prayNotification: PrayNotification)

    @Query("SELECT * FROM PrayNotification WHERE id = :id")
    fun getPrayNotification(id:Int = PRAY_NOTIFICATION_ID):Flow<PrayNotification>

    @Query("SELECT * FROM PrayNotification WHERE id = :id")
   suspend fun getPrayNotificationById(id:Int = PRAY_NOTIFICATION_ID):PrayNotification?
}