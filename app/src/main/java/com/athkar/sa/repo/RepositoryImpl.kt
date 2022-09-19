package com.athkar.sa.repo

import com.athkar.sa.db.AthkarDatabase
import com.athkar.sa.db.entity.*
import com.athkar.sa.models.AthkarCategory
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RepositoryImpl @Inject constructor(database: AthkarDatabase) : Repository {
    private val counterDao = database.counterAlthkerDao
    private val prayInfoDao = database.prayInfoDao
    private val prayNotificationDao = database.prayNotificationDao
    private val athkarDao = database.athkarDao
    private val favoriteAthkar = database.favoriteAlthker
    override suspend fun deleteAllTablePrayInfo() {
        prayInfoDao.deleteAllTable()
    }

    override suspend fun getPrayInfoByDayAndMonth(day: Int, month: Int): PrayInfo? {
        return prayInfoDao.getTodayPrayInfo(day, month)
    }

    override fun getPrayInfo(): Flow<List<PrayInfo>> {
        return prayInfoDao.getPrayInfo()
    }

    override suspend fun addPrayInfo(prayInfo: PrayInfo) {
        prayInfoDao.addPrayInfo(prayInfo)
    }


    override suspend fun removePrayInfo(prayInfo: PrayInfo) {
        prayInfoDao.removePrayInfo(prayInfo)
    }

    override fun getCounterAlthker(): Flow<List<CounterAlthker>> {
        return counterDao.getCounterAlthker()
    }

    override suspend fun updateCounterAlthker(counterAlthker: CounterAlthker) {
        counterDao.updateCount(counterAlthker)
    }

    override suspend fun removeCounterAlthker(counterAlthker: CounterAlthker) {
        counterDao.removeCounterAlthker(counterAlthker)
    }

    override suspend fun addNewCounterAlthker(counterAlthker: CounterAlthker) {
        counterDao.addNewCounterAlthker(counterAlthker)
    }

    override suspend fun addPrayNotification(prayNotification: PrayNotification) {
        prayNotificationDao.addPrayNotification(prayNotification)
    }

    override suspend fun updatePrayNotification(prayNotification: PrayNotification) {
        prayNotificationDao.updatePrayNotification(prayNotification)
    }

    override fun getPrayNotification(): Flow<PrayNotification> {
        return prayNotificationDao.getPrayNotification()
    }

    override suspend fun getPrayNotificationById(id: Int): PrayNotification? {
        return prayNotificationDao.getPrayNotificationById(id)
    }

    override suspend fun getAthkarByCategory(athkarCategory: AthkarCategory): List<Athkar> {
        return athkarDao.getAthkarByCategory(athkarCategory)
    }

    override fun getFavorites(): Flow<List<FavoriteAthkar>> {
        return favoriteAthkar.getFavorites()
    }

    override suspend fun addFavoriteAlthker(favoriteAthkar: FavoriteAthkar) {
        this.favoriteAthkar.addFavoriteAlthker(favoriteAthkar)
    }

    override suspend fun removeFavoriteAlthker(favoriteAthkar: FavoriteAthkar) {
        this.favoriteAthkar.removeFavoriteAlthker(favoriteAthkar)
    }

    override suspend fun removeFavoriteAlthkerByName(nameAlthker: String,category: AthkarCategory) {
        favoriteAthkar.removeFavoriteAlthkerByName(nameAlthker,category)
    }
}