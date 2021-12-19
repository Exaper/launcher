package com.exaper.launcher.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.exaper.launcher.R
import com.exaper.launcher.viewmodel.LauncherViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LauncherFragment : Fragment(R.layout.main_fragment) {
    private val viewModel by viewModels<LauncherViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.hashCode()
    }

    companion object {
        fun newInstance() = LauncherFragment()
    }
}
