package com.athkar.sa.ui.homeScreen.athkar

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.athkar.sa.db.entity.Athkar
import com.athkar.sa.db.entity.FavoriteAthkar
import com.athkar.sa.models.AthkarCategory
import com.athkar.sa.repo.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AthkarViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    private val _athkar = MutableLiveData<List<Athkar>>()
    val athkar: LiveData<List<Athkar>> = _athkar

    data class AthkarStateUI(
        val currentNameAlthker: String,
        val currentFavorite: FavoriteAthkar? = null
    )

    private val getFaviorate = repository.getFavorites()

    private val _times = MutableLiveData(0)
    val times :LiveData<Int> = _times


    private val _favorite = MutableStateFlow<FavoriteAthkar?>(null)
    val favorite = _favorite.asStateFlow()
    var currentNameOfAlthker = ""

    private val _eventClick = MutableLiveData<Unit>()
    val eventClick: LiveData<Unit> = _eventClick

    private val _moveToPosition = MutableLiveData<String>()
    val moveToPosition: LiveData<String> = _moveToPosition
    private var mFavoriteList = emptyList<FavoriteAthkar>()
    var categoryAlthker: AthkarCategory? = null

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getFaviorate.collect {
                mFavoriteList = it
                val hasFavorite =
                    it.find { it.nameAlthkr == currentNameOfAlthker && it.athkarCategory == categoryAlthker }
                _favorite.value = hasFavorite
            }
        }
    }

    fun removeFavorite(favoriteAthkar: FavoriteAthkar) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.removeFavoriteAlthker(favoriteAthkar)
        }
    }

    fun updateEvent() {
        _eventClick.value = Unit
    }

    fun addFavorite(favoriteAthkar: FavoriteAthkar) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addFavoriteAlthker(favoriteAthkar)
        }
    }

    fun updateTimesAlthker(times: Int) {
        _times.value = times
    }

    fun updateFavorite() {
        val hasFavorite =
            mFavoriteList.find { it.nameAlthkr == currentNameOfAlthker && it.athkarCategory == categoryAlthker }
        _favorite.value = hasFavorite
    }

    fun getAthkar(athkarCategory: AthkarCategory) {
        viewModelScope.launch(Dispatchers.IO) {
            val getAthkar = repository.getAthkarByCategory(athkarCategory)
                .sortedBy {
                    it.position
                }
            Log.d("Athkar ViewModel", "getAthkar: called athkart  : $getAthkar")
            _athkar.postValue(getAthkar)
        }
    }

    fun moveToPosition(nameAlthker: String) {
        _moveToPosition.value = nameAlthker
    }
}