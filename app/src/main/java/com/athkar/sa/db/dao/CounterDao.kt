package com.athkar.sa.db.dao

import androidx.room.*
import com.athkar.sa.db.entity.CounterAlthker
import kotlinx.coroutines.flow.Flow

@Dao
interface CounterDao {

    @Query("SELECT * FROM counteralthker ")
    fun getCounterAlthker(): Flow<List<CounterAlthker>>

    @Update
    suspend fun updateCount(counterAlthker:CounterAlthker)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNewCounterAlthker(counterAlthker: CounterAlthker)

    @Delete
    suspend fun removeCounterAlthker(counterAlthker: CounterAlthker)
}