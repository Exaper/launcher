package com.exaper.launcher

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class LauncherApplication : Application() {
    override fun onCreate() {
        super.onCreate()

    }
}