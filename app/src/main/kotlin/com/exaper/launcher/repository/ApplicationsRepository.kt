package com.exaper.launcher.repository

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.exaper.launcher.api.PolicyApiClient
import com.exaper.launcher.data.Launchable
import com.exaper.launcher.data.PolicyDao
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApplicationsRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val policyDao: PolicyDao,
    private val apiClient: PolicyApiClient
) {
    private val _launchablesFlow = MutableSharedFlow<List<Launchable>>(replay = 1)

    private val policies
        get() = policyDao.getDenyList().map { it.map { it.packageName }.toSet() } // Set will offer O(1) policy lookups.

    val launchables = _launchablesFlow
        .onSubscription { registerPackageUpdatesReceiver() }
        .onCompletion { unregisterPackageUpdatesReceiver() }
        .combine(policies) { appsList, denySet ->
            appsList.map { it.copy(restricted = denySet.contains(it.pkg)) }
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

    suspend fun update() = coroutineScope {
        awaitAll(async { loadApplications() }, async { refreshPolicy() }) // Build apps and refresh policy in parallel.
    }

    private suspend fun loadApplications() = withContext(IO) { // Loading icon/label for all apps will take time.
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

    private suspend fun refreshPolicy() {
        apiClient.getDenyList().onSuccess { policyDao.replaceDenyListWith(it) }
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