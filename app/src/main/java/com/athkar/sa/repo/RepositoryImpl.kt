package com.athkar.sa.repo

import com.athkar.sa.db.AthkarDatabase
import com.athkar.sa.db.entity.CounterAlthker
import com.athkar.sa.db.entity.PrayInfo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RepositoryImpl @Inject constructor(database: AthkarDatabase) : Repository {
    private val counterDao = database.counterAlthkerDao
    private val prayInfoDao = database.prayInfoDao
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
}