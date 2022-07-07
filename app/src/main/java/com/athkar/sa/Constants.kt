package com.athkar.sa

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object Constants {
    const val TOOLBAR_COLOR_ALPHA = 150
    const val DATABASE_NAME = "ATHKAR.db"
    const val BASE_URL = "https://api.aladhan.com/v1/"

    const val EMPTY_COUNTERALTHKER = "EMPTY"
     val COUNTER_AKTHKER_DATASTORE_KEY = stringPreferencesKey("counter althker key")
     val ENABLE_VIBRATE_KEY = booleanPreferencesKey("enable vibrate")
}