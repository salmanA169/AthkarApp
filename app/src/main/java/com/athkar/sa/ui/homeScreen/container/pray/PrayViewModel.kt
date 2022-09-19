package com.athkar.sa.ui.homeScreen.container.pray

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.athkar.sa.db.entity.DateToday
import com.athkar.sa.db.entity.PrayInfo
import com.athkar.sa.db.entity.PrayItems
import com.athkar.sa.db.entity.PrayName
import com.athkar.sa.repo.Repository
import com.athkar.sa.uitls.getDateToday
import com.athkar.sa.uitls.getPraysForCalendar
import com.athkar.sa.uitls.updatePrayNotification
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.chrono.HijrahDate
import java.time.temporal.ChronoField
import javax.inject.Inject

@HiltViewModel
class PrayViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    data class PrayStateUi(
        val praysToday: List<PrayItems> = emptyList(),
        val date: DateToday? = null,
        val orderPray: PrayInfo.OrderPray? = null
    )

    private val _prays = MutableLiveData<PrayStateUi>()

    val prays = _prays

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val date = HijrahDate.now()
            repository.getPrayInfoByDayAndMonth(
                date.get(ChronoField.DAY_OF_MONTH),
                date.get(ChronoField.MONTH_OF_YEAR)
            ).also { prayInfo ->
                if (prayInfo == null) {
                    _prays.postValue(PrayStateUi())
                } else {
                    repository.getPrayNotification().collect {
                        _prays.postValue(
                            PrayStateUi(
                                prayInfo.getPraysForCalendar(it),
                                prayInfo.getDateToday(),
                                prayInfo.getCurrentOrderPray()
                            )
                        )
                    }
                }
            }
        }
    }

    fun updatePrayNotification(prayName: PrayName,onFinish:()->Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val getCurrentPrayNotification = repository.getPrayNotification().first()
            repository.updatePrayNotification(
                prayName.updatePrayNotification(
                    getCurrentPrayNotification
                )
            )
        }.invokeOnCompletion {
            if (it ==null){
                onFinish()
            }
        }
    }

}