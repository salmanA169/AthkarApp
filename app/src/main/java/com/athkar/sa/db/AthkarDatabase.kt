package com.athkar.sa.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.athkar.sa.db.dao.CounterDao
import com.athkar.sa.db.dao.PrayInfoDao
import com.athkar.sa.db.entity.CounterAlthker
import com.athkar.sa.db.entity.PrayInfo

@Database(
    entities = [CounterAlthker::class,PrayInfo::class],
    version = 1
)
abstract class AthkarDatabase:RoomDatabase() {

    abstract val counterAlthkerDao:CounterDao
    abstract val prayInfoDao:PrayInfoDao
}