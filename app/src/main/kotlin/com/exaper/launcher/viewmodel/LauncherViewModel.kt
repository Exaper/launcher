package com.exaper.launcher.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.exaper.launcher.repository.ApplicationsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LauncherViewModel @Inject constructor(repository: ApplicationsRepository) : ViewModel() {

    val launchables = repository.launchables.asLiveData()

    init {
        viewModelScope.launch {
            repository.refreshApplications()
        }
    }

}