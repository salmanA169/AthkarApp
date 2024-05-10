package com.athkar.sa

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.content.res.Resources.Theme
import android.os.Build
import android.widget.RemoteViews
import com.athkar.sa.db.entity.PrayName
import com.athkar.sa.models.toCalendarPray
import com.athkar.sa.repo.Repository
import com.athkar.sa.uitls.ConstantPatternsDate
import com.athkar.sa.uitls.createPendingIntentActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.chrono.HijrahDate
import java.time.temporal.ChronoField
import javax.inject.Inject

@AndroidEntryPoint
class PrayerTimeWidget : AppWidgetProvider() {
    private val TAG: String? = javaClass.simpleName
    private val scope = CoroutineScope(SupervisorJob())

    @Inject
    lateinit var repository: Repository

    private val listIds = listOf(
        ViewIdsWidget(R.id.tv_asar, R.id.tv_asar_icon, R.id.tv_asar_time),
        ViewIdsWidget(R.id.tv_fajar, R.id.tv_fajar_icon, R.id.tv_fajar_time),
        ViewIdsWidget(R.id.tv_sunrise, R.id.tv_sunrise_icon, R.id.tv_sunrise_time),
        ViewIdsWidget(R.id.tv_duhar, R.id.tv_duhar_icon, R.id.tv_duhar_time),
        ViewIdsWidget(R.id.tv_maghrb, R.id.tv_maghrb_icon, R.id.tv_maghrb_time),
        ViewIdsWidget(R.id.tv_isha, R.id.tv_isha_icon, R.id.tv_isha_time)
    )

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        val dateEn = LocalDate.now()
        val dateAr = HijrahDate.now()
        val remoteViews = RemoteViews(context.packageName, R.layout.prayer_time_widget)
        remoteViews.setTextViewText(R.id.tv_date_en, dateEn.toString())
        remoteViews.setTextViewText(
            R.id.tv_date_ar,
            dateAr.format(ConstantPatternsDate.hijrahPattern)
        )
        remoteViews.setTextViewText(R.id.tv_today, dateAr.format(ConstantPatternsDate.todayPattern))
        val pendingIntentMainActivity =
            createPendingIntentActivity(context, Intent(context, MainActivity::class.java))
        remoteViews.setOnClickPendingIntent(android.R.id.background, pendingIntentMainActivity)
        getPrayTime(
            dateAr.get(ChronoField.DAY_OF_MONTH),
            dateAr.get(ChronoField.MONTH_OF_YEAR),
            remoteViews,
            repository,
            appWidgetId,
            appWidgetManager,
            context.resources,
            context.theme
        )

    }

    private fun getPrayTime(
        dayOfWeekAr: Int,
        monthOfYearAr: Int,
        remoteViews: RemoteViews,
        repository: Repository,
        widgetId: Int,
        appWidgetManager: AppWidgetManager,
        resource: Resources,
        theme: Theme
    ) {
        val pendingResult = goAsync()
        scope.launch(Dispatchers.IO) {
            val getPrayTime = repository.getPrayInfoByDayAndMonth(dayOfWeekAr, monthOfYearAr)
            if (getPrayTime == null) {
            } else {
                val getNextPray = getPrayTime.getCurrentOrderPray()
                val calendarPray = getPrayTime.toCalendarPray()
                remoteViews.setTextViewText(R.id.tv_fajar_time, calendarPray.fajarTime)
                remoteViews.setTextViewText(R.id.tv_sunrise_time, calendarPray.sunRiseTime)
                remoteViews.setTextViewText(R.id.tv_duhar_time, calendarPray.duharTime)
                remoteViews.setTextViewText(R.id.tv_asar_time, calendarPray.asarTime)
                remoteViews.setTextViewText(R.id.tv_maghrb_time, calendarPray.maghrabTime)
                remoteViews.setTextViewText(R.id.tv_isha_time, calendarPray.ishaTime)
                val getIdsView = getNextPrayIds(getNextPray.nextPray)
                changeColorById(
                    resource.getColor(R.color.currentPrayColor, theme),
                    resource.getColor(R.color.white, theme),
                    remoteViews,
                    getIdsView.tvPray,
                    getIdsView.iconId,
                    getIdsView.tvPrayTime
                )
            }
            appWidgetManager.updateAppWidget(widgetId, remoteViews)
            pendingResult.finish()
        }
    }

    private fun getNextPrayIds(nextPrayName: PrayName) = when (nextPrayName) {
        PrayName.FAJAR -> {
            ViewIdsWidget(R.id.tv_fajar, R.id.tv_fajar_icon, R.id.tv_fajar_time)
        }

        PrayName.SUNRISE -> {
            ViewIdsWidget(R.id.tv_sunrise_time, R.id.tv_sunrise_icon, R.id.tv_sunrise_time)
        }

        PrayName.DUHAR -> {
            ViewIdsWidget(R.id.tv_duhar, R.id.tv_duhar_icon, R.id.tv_duhar_time)
        }

        PrayName.ASAR -> {
            ViewIdsWidget(R.id.tv_asar, R.id.tv_asar_icon, R.id.tv_asar_time)
        }

        PrayName.MAGHRAB -> {
            ViewIdsWidget(R.id.tv_maghrb, R.id.tv_maghrb_icon, R.id.tv_maghrb_time)
        }

        PrayName.ISHA -> {
            ViewIdsWidget(R.id.tv_isha, R.id.tv_isha_icon, R.id.tv_isha_time)
        }
    }

    private fun changeColorById(
        color: Int,
        defaultColor: Int,
        remoteViews: RemoteViews,
        prayId: Int,
        iconId: Int,
        prayTime: Int,
    ) {
        removePreviousPray(
            listIds,
            prayId,
            iconId,
            prayTime,
            defaultColor,
            remoteViews
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            remoteViews.setColor(prayId, "setTextColor", R.color.currentPrayColor)
            remoteViews.setColor(prayTime, "setTextColor", R.color.currentPrayColor)
            remoteViews.setColor(
                iconId,
                "setColorFilter",
                R.color.currentPrayColor
            )
        } else {
            remoteViews.setTextColor(prayId, color)
            remoteViews.setTextColor(prayTime, color)
            remoteViews.setInt(
                iconId,
                "setColorFilter",
                color
            )
        }
    }

    private fun removePreviousPray(
        listIds: List<ViewIdsWidget>,
        prayId: Int,
        iconId: Int,
        prayTime: Int,
        color: Int,
        remoteViews: RemoteViews
    ) {
        val newList = listIds.toMutableList()
        newList.remove(ViewIdsWidget(prayId, iconId, prayTime))
        newList.forEach {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                remoteViews.setColor(it.tvPrayTime, "setTextColor", R.color.white)
                remoteViews.setColor(it.tvPray, "setTextColor", R.color.white)
                remoteViews.setColor(
                    it.iconId,
                    "setColorFilter",
                    R.color.white
                )
            } else {
                remoteViews.setTextColor(it.tvPrayTime, color)
                remoteViews.setTextColor(it.tvPray, color)
                remoteViews.setInt(
                    it.iconId,
                    "setColorFilter",
                    color
                )
            }
        }
    }

    data class ViewIdsWidget(val tvPray: Int, val iconId: Int, val tvPrayTime: Int)
}
