package com.athkar.sa.ui.homeScreen.container.counter

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.athkar.sa.db.entity.CounterAlthker
import com.athkar.sa.repo.Repository
import com.athkar.sa.uitls.dataStoreSettings
import com.athkar.sa.uitls.updateCounterAlthker
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChooseCounterViewModel @Inject constructor(
    @ApplicationContext context : Context,
    private val  repository: Repository
) : ViewModel() {
    private val _counters = MutableStateFlow(emptyList<CounterAlthker>())
    val counters = _counters.asStateFlow()

    private val _popBack = MutableLiveData<Boolean>()
    val popBack :LiveData<Boolean> = _popBack

    private val dataStoreSettings = context.dataStoreSettings
    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getCounterAlthker().collect {
                _counters.emit(it)
            }
        }
    }

    fun refreshCounters(counters:List<CounterAlthker>,onComplete:()->Unit){
        viewModelScope.launch(Dispatchers.IO) {
            val checkCounter = counters.all { it.count == 0 }
            if (!checkCounter){
                counters.forEach {
                    repository.updateCounterAlthker(it.copy(count = 0))
                }
            }else{
                cancel("counters 0")
            }
        }.invokeOnCompletion {
            if (it == null){
                onComplete()
            }
        }
    }
    fun addNewCounter(counterAlthker: CounterAlthker){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addNewCounterAlthker(counterAlthker)
        }
    }
    fun saveNewCounter(name:String){
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreSettings.updateCounterAlthker(name)
        }.invokeOnCompletion {
            _popBack.postValue(true)
        }
    }
    fun removeCounter(counterAlthker: CounterAlthker){
        viewModelScope.launch(Dispatchers.IO) {
            repository.removeCounterAlthker(counterAlthker)
        }
    }
}