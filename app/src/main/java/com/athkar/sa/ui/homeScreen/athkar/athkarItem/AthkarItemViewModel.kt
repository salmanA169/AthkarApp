package com.athkar.sa.ui.homeScreen.athkar.athkarItem

import androidx.lifecycle.ViewModel
import com.athkar.sa.repo.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AthkarItemViewModel @Inject constructor(repository: Repository) : ViewModel()