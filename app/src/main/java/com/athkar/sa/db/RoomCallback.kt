package com.athkar.sa.db

import android.content.Context
import android.util.Log
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.athkar.sa.db.entity.Athkar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

class RoomCallback @Inject constructor(@ApplicationContext private val context: Context, private val db:Provider<AthkarDatabase>):RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        try {

            context.assets.open("athkar.json").use {
                com.google.gson.stream.JsonReader(it.reader()).use { ii->
                    val athkarType = object : TypeToken<List<Athkar>>(){}.type
                    val athkars = Gson().fromJson<List<Athkar>>(ii,athkarType)
                    val getDatabase = this.db.get()
                    CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
                        athkars.forEach {
                            getDatabase.athkarDao.addAthkar(it)
                        }
                    }
                }
            }
        }catch (e:Exception){

        }

    }
}