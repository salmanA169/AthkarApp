package com.athkar.sa.uitls

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import com.athkar.sa.PrayerTimeWidget


fun updateWidgets(context:Context){
    val appWidgetManager = AppWidgetManager.getInstance(context)
    val idsWidget = appWidgetManager.getAppWidgetIds(ComponentName(context,PrayerTimeWidget::class.java))
    val intent = Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE).apply {
        putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,idsWidget)
    }
    context.sendBroadcast(intent)
}