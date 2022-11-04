package com.athkar.sa

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.widget.ImageView
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.os.BuildCompat
import com.athkar.sa.uitls.ConstantPatternsDate
import com.athkar.sa.uitls.getAlarmNotificationPendingIntent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import java.time.LocalDate
import java.time.chrono.HijrahDate

class PrayerTimeWidget : AppWidgetProvider() {
    private val TAG: String? = javaClass.simpleName
    private val scope = CoroutineScope(SupervisorJob())

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onDeleted(context: Context?, appWidgetIds: IntArray?) {
        super.onDeleted(context, appWidgetIds)
        Log.d("PrayerTimeWidget", "onDeleted: called")
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        val dateEn = LocalDate.now()
        val dateAr = HijrahDate.now()
        val remoteViews = RemoteViews(context.packageName,R.layout.prayer_time_widget)

        remoteViews.setTextViewText(R.id.tv_date_en,dateEn.toString())
        remoteViews.setTextViewText(R.id.tv_date_ar,dateAr.format(ConstantPatternsDate.hijrahPattern))
        remoteViews.setTextViewText(R.id.tv_today,dateAr.format(ConstantPatternsDate.todayPattern))


        appWidgetManager.updateAppWidget(appWidgetId,remoteViews)
    }
}
