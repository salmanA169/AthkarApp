package com.athkar.sa.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.athkar.sa.db.convertor.Convertors
import com.athkar.sa.db.dao.*
import com.athkar.sa.db.entity.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonObject

@Database(
    entities = [Athkar::class, CounterAlthker::class, PrayInfo::class, PrayNotification::class, FavoriteAthkar::class,SurahDownloadEntity::class],
    version = 3,
    autoMigrations = [AutoMigration(from = 1 , to = 2)]
)
@TypeConverters(Convertors::class)
abstract class AthkarDatabase : RoomDatabase() {

    abstract val counterAlthkerDao: CounterDao
    abstract val prayInfoDao: PrayInfoDao
    abstract val prayNotificationDao: PrayNotificationDao
    abstract val athkarDao: AthkarDao
    abstract val favoriteAlthker: FavoriteDao
    abstract val surahDownloadEntity:SurahDownloadDao
}