package com.athkar.sa.uitls

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.athkar.sa.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStoreSettings: DataStore<Preferences> by preferencesDataStore(name = "settings")
val Context.quranSettingsDataStore :DataStore<Preferences> by preferencesDataStore(name = "quran-settings")
fun DataStore<Preferences>.getCurrentCounterAlthker():Flow<String>{
    return data.map {
        it[Constants.COUNTER_AKTHKER_DATASTORE_KEY]?: Constants.EMPTY_COUNTERALTHKER
    }
}

suspend fun DataStore<Preferences>.updateCounterAlthker(name:String){
    edit {
        it[Constants.COUNTER_AKTHKER_DATASTORE_KEY] = name
    }
}
fun DataStore<Preferences>.getCurrentEnableVibrate():Flow<Boolean>{
    return data.map { it[Constants.ENABLE_VIBRATE_KEY]?:false }
}
suspend fun DataStore<Preferences>.updateEnableVibrate(enable:Boolean){
    edit {
        it[Constants.ENABLE_VIBRATE_KEY] = enable
    }
}

