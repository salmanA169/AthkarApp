package com.athkar.sa.ui.homeScreen.container.counter

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.athkar.sa.db.entity.CounterAlthker
import com.athkar.sa.repo.Repository
import com.athkar.sa.uitls.updateCounterAlthker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChooseCounterViewModel @Inject constructor(
    private val repository: Repository,
    private val dataStoreSettings: DataStore<Preferences>
) : ViewModel() {
    val counters = repository.getCounterAlthker()

    private val _popBack = MutableLiveData<Boolean>()
    val popBack: LiveData<Boolean> = _popBack


    fun refreshCounters(counters: List<CounterAlthker>, onComplete: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val checkCounter = counters.all { it.count == 0 }
            if (!checkCounter) {
                counters.forEach {
                    repository.updateCounterAlthker(it.copy(count = 0))
                }
            } else {
                cancel("counters 0")
            }
        }.invokeOnCompletion {
            if (it == null) {
                onComplete()
            }
        }
    }

    fun addNewCounter(counterAlthker: CounterAlthker) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addNewCounterAlthker(counterAlthker)
        }
    }

    fun saveNewCounter(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreSettings.updateCounterAlthker(name)
        }.invokeOnCompletion {
            _popBack.postValue(true)
        }
    }

    fun removeCounter(counterAlthker: CounterAlthker) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.removeCounterAlthker(counterAlthker)
        }
    }
}