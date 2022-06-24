package com.athkar.sa.ui.homeScreen.container.counter

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.athkar.sa.db.entity.CounterAlthker
import com.athkar.sa.repo.Repository
import com.athkar.sa.uitls.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CounterViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val repository: Repository
) : ViewModel() {

    data class CounterStateUI(val counterAlthker: CounterAlthker?,val enableVibrate:Boolean)

    private val dataStoreSettings = context.dataStoreSettings
    private val currentCounterAltker = MutableStateFlow<List<CounterAlthker>?>(null)
    val currentCounterAlthker =
        combine(currentCounterAltker, dataStoreSettings.getCurrentCounterAlthker(),dataStoreSettings.getCurrentEnableVibrate()) { i, o,enable ->
            val find = i?.find { it.name == o }
            CounterStateUI(find,enable)
        }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getCounterAlthker().collect {
                currentCounterAltker.emit(it)
            }
        }
    }

    fun addNewCounterAlthker(counterAlthker: CounterAlthker) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addNewCounterAlthker(counterAlthker)
        }
    }

    fun updateCounterAlthker(counterAlthker: CounterAlthker) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateCounterAlthker(counterAlthker)
        }
    }

    fun saveCurrentCounterAlthker(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreSettings.updateCounterAlthker(name)
        }
    }

    fun updateEnableVibration(enable:Boolean){
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreSettings.updateEnableVibrate(enable)
        }
    }
}