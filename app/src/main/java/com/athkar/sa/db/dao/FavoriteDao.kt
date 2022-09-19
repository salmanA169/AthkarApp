package com.athkar.sa.db.dao

import androidx.room.*
import com.athkar.sa.db.entity.FavoriteAthkar
import com.athkar.sa.models.AthkarCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favoriteathkar")
     fun getFavorites():Flow<List<FavoriteAthkar>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavoriteAlthker(favoriteAthkar: FavoriteAthkar)

    @Delete
    suspend fun removeFavoriteAlthker(favoriteAthkar: FavoriteAthkar)

    @Query("DELETE FROM favoriteathkar WHERE nameAlthkr = :nameAlthker AND athkarCategory = :category ")
    suspend fun removeFavoriteAlthkerByName(nameAlthker:String,category: AthkarCategory)
}