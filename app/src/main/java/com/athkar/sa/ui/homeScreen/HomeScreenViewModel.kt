package com.athkar.sa.ui.homeScreen

import android.app.NotificationManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.athkar.sa.db.entity.DateToday
import com.athkar.sa.db.entity.PrayInfo
import com.athkar.sa.repo.Repository
import com.athkar.sa.uitls.cancelAllNotification
import com.athkar.sa.uitls.getDateToday
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.chrono.HijrahDate
import java.time.temporal.ChronoField
import javax.inject.Inject
import kotlin.properties.Delegates

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val repository: Repository,
    notificationManager: NotificationManager
) : ViewModel() {

    data class HomeScreenStateUI(
        val prayToday: PrayInfo.OrderPray,
        val dateToday: DateToday
    )

    private val _athkar = MutableLiveData<List<UiHomeScreens>>()
    val athkar: LiveData<List<UiHomeScreens>> = _athkar

    private var _prays = MutableLiveData<HomeScreenStateUI>()
    val prays: LiveData<HomeScreenStateUI> = _prays

    fun updateData() {
        val dateHijre = HijrahDate.now()
        viewModelScope.launch(Dispatchers.IO) {
            repository.getPrayInfoByDayAndMonth(
                dateHijre.get(ChronoField.DAY_OF_MONTH),
                dateHijre.get(ChronoField.MONTH_OF_YEAR)
            ).also { prayInfo ->
                if (prayInfo != null){
                        val order = prayInfo.getCurrentOrderPray()
                        _prays.postValue(HomeScreenStateUI(order, prayInfo.getDateToday()))
                }else{
                    repository.getPrayInfo().collect{listPrayInfo->
                        val findCurrentDate = listPrayInfo.find {
                            it.day == dateHijre.get(ChronoField.DAY_OF_MONTH)
                                    && it.month ==  dateHijre.get(ChronoField.MONTH_OF_YEAR)
                        }
                        findCurrentDate?.let {
                            _prays.postValue(HomeScreenStateUI(it.getCurrentOrderPray(), it.getDateToday()))
                            cancel()
                        }
                    }
                }
            }

        }
    }

    init {
        notificationManager.cancelAllNotification()
        _athkar.value = bindScreens()
        updateData()
    }

}