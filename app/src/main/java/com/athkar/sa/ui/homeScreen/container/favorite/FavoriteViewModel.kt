package com.athkar.sa.ui.homeScreen.container.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.athkar.sa.db.entity.FavoriteAthkar
import com.athkar.sa.models.AthkarCategory
import com.athkar.sa.repo.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    val favorites = repository.getFavorites()


    fun removeAlthker(favoriteAthkar: FavoriteAthkar){
        viewModelScope.launch(Dispatchers.IO) {
            repository.removeFavoriteAlthker(favoriteAthkar)
        }
    }
}
