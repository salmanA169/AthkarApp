package com.athkar.sa.ui.homeScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.athkar.sa.models.Athkar
import com.athkar.sa.models.getAthkar
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor() : ViewModel() {
    private val _athkar =MutableLiveData<List<UiHomeScreens>>()
    val athkar :LiveData<List<UiHomeScreens>> = _athkar

    init {
        _athkar.value = bindScreens()
    }

}