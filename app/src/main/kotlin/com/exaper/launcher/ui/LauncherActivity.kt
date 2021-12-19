package com.exaper.launcher.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.exaper.launcher.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LauncherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, LauncherFragment.newInstance())
                .commitNow()
        }
    }
}