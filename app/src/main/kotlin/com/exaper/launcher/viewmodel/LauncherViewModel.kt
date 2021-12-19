package com.exaper.launcher.viewmodel

import androidx.lifecycle.ViewModel
import com.exaper.launcher.repository.ApplicationsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LauncherViewModel @Inject constructor(repository : ApplicationsRepository) : ViewModel() {

}