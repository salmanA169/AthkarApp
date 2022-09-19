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

    const val NOTIFICATION_CHANNEL_ID = "Pray Notification"
    const val ALARM_DATA_PRAY_NAME = "pray_name"

    const val ATHKAR_BUNDLE_KEY_OBJECT = "athkar"
    const val ITEM_BOTTOM_KEY = "item_bottom_key"
    const val COLOR_ALTHKER_BOTTOM_KEY = "color_althker_bottom_key"
    const val TITLE_BOTTOM_KEY = "title_bottom_key"
}