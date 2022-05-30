package com.athkar.sa.ui.homeScreen.container.pray

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.athkar.sa.db.entity.DateToday
import com.athkar.sa.db.entity.Pray
import com.athkar.sa.db.entity.PrayInfo
import com.athkar.sa.db.entity.PrayNotification
import com.athkar.sa.uitls.getDateToday
import com.athkar.sa.uitls.getPrays
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.time.LocalTime
import java.time.OffsetTime
import javax.inject.Inject

@HiltViewModel
class PrayViewModel @Inject constructor() : ViewModel() {

    private val _prays = MutableLiveData<List<Pray>>()
    val prays: LiveData<List<Pray>> = _prays
    val fakeDate = LocalDate.now().toEpochDay()
    val fakeFajer = LocalTime.parse("03:44").toSecondOfDay().toLong()
    val fakeSunrise = LocalTime.parse("05:10").toSecondOfDay().toLong()
    val fakeDuhar = LocalTime.parse("11:49").toSecondOfDay().toLong()
    val fakeAsar = LocalTime.parse("15:15").toSecondOfDay().toLong()
    val fakeMughrab = LocalTime.parse("18:28").toSecondOfDay().toLong()
    val fakeIsha = LocalTime.parse("19:58").toSecondOfDay().toLong()
    val prayInfoTest = PrayInfo(
        fakeDate,
        "الرياض",
        fakeFajer,
        fakeSunrise,
        fakeDuhar,
        fakeAsar,
        fakeMughrab,
        fakeIsha
    )
    private val _prayInfo = MutableLiveData(prayInfoTest.getDateToday())
    val prayInfo:LiveData<DateToday> = _prayInfo

    // TODO: add combine flow for prayNotification and pray Info
    init {
        _prays.value = prayInfoTest.getPrays(PrayNotification(
            fajer = true,
            sunRise = false,
            duhar = false,
            asar = false,
            maghrab = false,
            isha = false
        ))
    }
}