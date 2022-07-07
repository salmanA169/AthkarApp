package com.athkar.sa.repo

import com.athkar.sa.db.entity.CounterAlthker
import com.athkar.sa.db.entity.PrayInfo
import kotlinx.coroutines.flow.Flow

interface Repository {
    fun getCounterAlthker(): Flow<List<CounterAlthker>>
    suspend fun updateCounterAlthker(counterAlthker: CounterAlthker)
    suspend fun removeCounterAlthker(counterAlthker: CounterAlthker)
    suspend fun addNewCounterAlthker(counterAlthker: CounterAlthker)

    fun getPrayInfo(): Flow<List<PrayInfo>>

    suspend fun addPrayInfo(prayInfo: PrayInfo)

    suspend fun removePrayInfo(prayInfo: PrayInfo)
}