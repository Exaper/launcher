package com.exaper.launcher.data

import android.content.Intent
import android.graphics.drawable.Drawable

data class Launchable(val name: CharSequence, val icon: Drawable, val launchIntent: Intent)