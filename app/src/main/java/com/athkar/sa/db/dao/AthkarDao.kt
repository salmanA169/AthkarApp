package com.athkar.sa.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.athkar.sa.db.entity.Athkar
import com.athkar.sa.models.AthkarCategory

@Dao
interface AthkarDao {
    @Query("SELECT * FROM athkar WHERE category = :category")
    suspend fun getAthkarByCategory(category: AthkarCategory):List<Athkar>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAthkar(athkar: Athkar)


}