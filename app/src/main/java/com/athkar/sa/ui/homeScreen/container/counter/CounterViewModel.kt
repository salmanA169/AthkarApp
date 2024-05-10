package com.athkar.sa.ui.homeScreen.container.counter

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.athkar.sa.db.entity.CounterAlthker
import com.athkar.sa.di.SettingsDataStore
import com.athkar.sa.repo.Repository
import com.athkar.sa.uitls.getCurrentCounterAlthker
import com.athkar.sa.uitls.getCurrentEnableVibrate
import com.athkar.sa.uitls.updateCounterAlthker
import com.athkar.sa.uitls.updateEnableVibrate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CounterViewModel @Inject constructor(
    private val repository: Repository,
    @SettingsDataStore private val dataStoreSettings: DataStore<Preferences>
) : ViewModel() {

    data class CounterStateUI(val counterAlthker: CounterAlthker?, val enableVibrate: Boolean)

    private val currentCounterAltker = repository.getCounterAlthker()
    val currentCounterAlthker =
        combine(
            currentCounterAltker,
            dataStoreSettings.getCurrentCounterAlthker(),
            dataStoreSettings.getCurrentEnableVibrate()
        ) { i, o, enable ->
            val find = i.find { it.name == o }
            CounterStateUI(find, enable)
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

    fun updateEnableVibration(enable: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreSettings.updateEnableVibrate(enable)
        }
    }
}