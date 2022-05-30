package com.athkar.sa.ui.homeScreen.container.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.athkar.sa.db.entity.FavoriteAthkar
import com.athkar.sa.models.AthkarCategory

class FavoriteViewModel : ViewModel() {
    private val _favorites = MutableLiveData<List<FavoriteAthkar>>()
    val favorite :LiveData<List<FavoriteAthkar>> = _favorites

    init {
        val test = buildList<FavoriteAthkar> {
            add(FavoriteAthkar("سورة الكهف",AthkarCategory.PRAY))
            add(FavoriteAthkar("سورة الكهف",AthkarCategory.MORNING))
            add(FavoriteAthkar("سورة الكهف",AthkarCategory.MY_ATHKAR))
            add(FavoriteAthkar("سورة الكهف",AthkarCategory.PRAY))
        }
        _favorites.value = test
    }
}