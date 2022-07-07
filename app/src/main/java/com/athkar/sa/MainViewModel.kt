package com.athkar.sa

import android.content.Context
import androidx.lifecycle.ViewModel
import com.athkar.sa.repo.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    @ApplicationContext context:Context,
   private val repository: Repository
):ViewModel() {

    val getPrayInfo = repository.getPrayInfo()
}