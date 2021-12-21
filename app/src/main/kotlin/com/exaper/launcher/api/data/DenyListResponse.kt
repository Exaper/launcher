package com.exaper.launcher.api.data

import com.squareup.moshi.Json

data class DenyListResponse(@Json(name = "denylist") val denylist : List<String>)
