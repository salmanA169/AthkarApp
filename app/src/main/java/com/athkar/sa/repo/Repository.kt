package com.athkar.sa.repo

import com.athkar.sa.db.entity.CounterAlthker
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun getCounterAlthker(): Flow<List<CounterAlthker>>
    suspend fun updateCounterAlthker(counterAlthker: CounterAlthker)

    suspend fun removeCounterAlthker(counterAlthker: CounterAlthker)

    suspend fun addNewCounterAlthker(counterAlthker: CounterAlthker)
}