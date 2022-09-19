package com.athkar.sa.repo

import com.athkar.sa.db.entity.*
import com.athkar.sa.models.AthkarCategory
import kotlinx.coroutines.flow.Flow

interface Repository {
    fun getCounterAlthker(): Flow<List<CounterAlthker>>
    suspend fun updateCounterAlthker(counterAlthker: CounterAlthker)
    suspend fun removeCounterAlthker(counterAlthker: CounterAlthker)
    suspend fun addNewCounterAlthker(counterAlthker: CounterAlthker)

    suspend fun getPrayInfoByDayAndMonth(day: Int, month: Int): PrayInfo?
    fun getPrayInfo(): Flow<List<PrayInfo>>
    suspend fun addPrayInfo(prayInfo: PrayInfo)
    suspend fun removePrayInfo(prayInfo: PrayInfo)
    suspend fun deleteAllTablePrayInfo()

    suspend fun addPrayNotification(prayNotification: PrayNotification)
    suspend fun updatePrayNotification(prayNotification: PrayNotification)
    fun getPrayNotification():Flow<PrayNotification>
   suspend fun getPrayNotificationById(id:Int = PRAY_NOTIFICATION_ID):PrayNotification?

   suspend fun getAthkarByCategory(athkarCategory: AthkarCategory):List<Athkar>

   fun getFavorites():Flow<List<FavoriteAthkar>>
   suspend fun addFavoriteAlthker(favoriteAthkar: FavoriteAthkar)
   suspend fun removeFavoriteAlthker(favoriteAthkar: FavoriteAthkar)
   suspend fun removeFavoriteAlthkerByName(nameAlthker:String,category: AthkarCategory)
}