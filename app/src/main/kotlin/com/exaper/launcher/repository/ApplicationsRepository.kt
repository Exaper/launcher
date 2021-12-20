package com.exaper.launcher.repository

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.exaper.launcher.data.Launchable
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApplicationsRepository @Inject constructor(@ApplicationContext private val context: Context) {
    private val _launchablesFlow = MutableSharedFlow<List<Launchable>>(replay = 1)
    val launchables: Flow<List<Launchable>> = _launchablesFlow.asSharedFlow().onSubscription {
        registerPackageUpdatesReceiver()
    }.onCompletion {
        unregisterPackageUpdatesReceiver()
    }

    private val packageUpdatesBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent != null) {
                val packageName = intent.data?.schemeSpecificPart
                if (packageName != null) {
                    when (intent.action) {
                        Intent.ACTION_PACKAGE_ADDED -> onPackageAdded(packageName)
                        Intent.ACTION_PACKAGE_REMOVED -> onPackageRemoved(packageName)
                    }
                }
            }
        }
    }

    suspend fun loadApplications() = withContext(IO) { // Loading icon/label from other apps will take time.
        val launchables = mutableListOf<Launchable>()
        val pm = context.packageManager
        pm.queryIntentActivities(Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER), 0).forEach {
            val launchable = resolveLaunchableFromPackageName(it.activityInfo.packageName)
            if (launchable != null) {
                launchables.add(launchable)
                _launchablesFlow.emit(ArrayList(launchables)) // Ensure internal list stays with us.
            }
        }
    }

    private fun onPackageAdded(packageName: String) {
        val newLaunchable = resolveLaunchableFromPackageName(packageName)
        if (newLaunchable != null) {
            val newList = ArrayList(_launchablesFlow.replayCache.last())
            newList.add(newLaunchable)
            _launchablesFlow.tryEmit(newList)
        }
    }

    private fun onPackageRemoved(packageName: String) {
        val newList = ArrayList(_launchablesFlow.replayCache.last())
        newList.removeIf { it.pkg == packageName }
        _launchablesFlow.tryEmit(newList)
    }

    private fun resolveLaunchableFromPackageName(packageName: String): Launchable? {
        val pm = context.packageManager
        val intent = pm.getLaunchIntentForPackage(packageName)
        if (intent != null && intent.component != null) {
            return Launchable(packageName, pm.getActivityInfo(intent.component!!, 0).loadLabel(pm), pm.getActivityIcon(intent), intent)
        }
        return null
    }

    private fun registerPackageUpdatesReceiver() {
        val intentFilter = IntentFilter().apply {
            addAction(Intent.ACTION_PACKAGE_ADDED)
            addAction(Intent.ACTION_PACKAGE_REMOVED)
            addDataScheme("package")
        }
        context.registerReceiver(packageUpdatesBroadcastReceiver, intentFilter)
    }

    private fun unregisterPackageUpdatesReceiver() {
        context.unregisterReceiver(packageUpdatesBroadcastReceiver)
    }
}