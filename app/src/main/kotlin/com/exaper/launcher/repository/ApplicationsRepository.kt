package com.exaper.launcher.repository

import android.content.Context
import android.content.Intent
import com.exaper.launcher.data.Launchable
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApplicationsRepository @Inject constructor(@ApplicationContext private val context: Context) {
    private val _launchablesFlow = MutableSharedFlow<List<Launchable>>(replay = 0)
    val launchables: Flow<List<Launchable>> = _launchablesFlow

    suspend fun refreshApplications() = withContext(IO) {
        val launchables = mutableListOf<Launchable>()
        val pm = context.packageManager
        val activities = pm.queryIntentActivities(Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER), 0)
        activities.forEach {
            val intent = pm.getLaunchIntentForPackage(it.resolvePackageName)
            if (intent != null) {
                launchables.add(Launchable(it.loadLabel(pm), it.loadIcon(pm), intent))
                _launchablesFlow.emit(launchables)
            }
        }
    }
}