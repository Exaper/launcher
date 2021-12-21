package com.exaper.launcher.api.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "deny_policy")
data class DenyPolicy(
    @PrimaryKey
    val packageName: String
)