package com.exaper.launcher.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.exaper.launcher.api.data.DenyPolicy

@Database(version = 1, exportSchema = false, entities = [DenyPolicy::class])
abstract class LauncherDatabase : RoomDatabase() {
    abstract fun policyDao(): PolicyDao
}