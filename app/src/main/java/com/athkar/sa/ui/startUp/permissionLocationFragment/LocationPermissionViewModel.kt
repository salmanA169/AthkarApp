package com.athkar.sa.ui.startUp.permissionLocationFragment

import androidx.lifecycle.ViewModel
import com.athkar.sa.repo.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LocationPermissionViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel()