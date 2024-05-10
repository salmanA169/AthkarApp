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
            addAthkarFromAsset(context,this.db.get())
        }catch (e:Exception){
            Log.e("RoomCallBack", "onCreate: error", e)
        }

    }

    override fun onOpen(db: SupportSQLiteDatabase) {
        super.onOpen(db)
        addAthkarFromAsset(context,this.db.get())
    }
    override fun onDestructiveMigration(db: SupportSQLiteDatabase) {
        super.onDestructiveMigration(db)
        addAthkarFromAsset(context,this.db.get())
    }
}
private fun addAthkarFromAsset(context:Context,db:AthkarDatabase){
    context.assets.open("athkar.json").use {
        com.google.gson.stream.JsonReader(it.reader()).use { ii->
            val athkarType = object : TypeToken<List<Athkar>>(){}.type
            val athkars = Gson().fromJson<List<Athkar>>(ii,athkarType)
            CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
                val getAthkar = db.athkarDao.getAllAthkar()
                if (getAthkar.isEmpty()){
                    athkars.forEach {
                        db.athkarDao.addAthkar(it)
                    }
                }
            }
        }
    }
}