package com.exaper.launcher.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApplicationsRepository @Inject constructor(@ApplicationContext context: Context) {
}