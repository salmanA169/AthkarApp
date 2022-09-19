package com.athkar.sa

import com.athkar.sa.db.entity.CounterAlthker
import com.athkar.sa.db.entity.PrayInfo
import com.athkar.sa.repo.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class FakeRepo @Inject constructor():Repository {
    init {
        println("called")
    }
    override fun getCounterAlthker(): Flow<List<CounterAlthker>> {
        return flowOf()
    }

    override suspend fun updateCounterAlthker(counterAlthker: CounterAlthker) {
        TODO("Not yet implemented")
    }

    override suspend fun removeCounterAlthker(counterAlthker: CounterAlthker) {
        TODO("Not yet implemented")
    }

    override suspend fun addNewCounterAlthker(counterAlthker: CounterAlthker) {
        TODO("Not yet implemented")
    }

    override suspend fun getPrayInfoByDayAndMonth(day: Int, month: Int): PrayInfo {
        return PrayInfo(0,0,"test",0,0,0,0,0,0,0,0)
    }

    override fun getPrayInfo(): Flow<List<PrayInfo>> {
       return flowOf(listOf())
    }

    override suspend fun addPrayInfo(prayInfo: PrayInfo) {
        TODO("Not yet implemented")
    }

    override suspend fun removePrayInfo(prayInfo: PrayInfo) {
        TODO("Not yet implemented")
    }
}