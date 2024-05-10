package com.athkar.sa.quran

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.athkar.sa.di.QuranSettingsDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class QuranSettings @Inject constructor(
    @QuranSettingsDataStore private val dataStore: DataStore<Preferences>
) {

    companion object{
        private val versionsQuranPages = intPreferencesKey("quran-pages-version")
        private val darkModePage = booleanPreferencesKey("quran-dark-mode")
    }

    suspend fun updateVersionQuranPage(version:Int) {
        dataStore.edit {
            it[versionsQuranPages] = version
        }
    }

    suspend fun getCurrentVersion() = dataStore.data.map { it[versionsQuranPages]?:-1 }.first()

    suspend fun updateDarkModePage(darkMode:Boolean){
        dataStore.edit {
            it[darkModePage] = darkMode
        }
    }
    suspend fun getCurrentDarkMode() = dataStore.data.map { it[darkModePage]?:false }.first()
}