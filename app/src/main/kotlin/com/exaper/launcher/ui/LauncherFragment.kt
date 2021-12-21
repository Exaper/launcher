package com.exaper.launcher.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.exaper.launcher.R
import com.exaper.launcher.databinding.FragmentLauncherBinding
import com.exaper.launcher.viewmodel.LauncherViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LauncherFragment : Fragment(R.layout.fragment_launcher) {
    private lateinit var binding: FragmentLauncherBinding
    private val viewModel by viewModels<LauncherViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = LaunchablesAdapter()
        binding = FragmentLauncherBinding.bind(view).apply {
            recyclerView.adapter = adapter
        }

        adapter.onItemClicked = {
            if (it.restricted) {
                showRestrictedApplicationDialog()
            } else {
                startActivity(it.launchIntent)
            }
        }

        viewModel.launchables.observe(viewLifecycleOwner) { adapter.items = it }
    }

    private fun showRestrictedApplicationDialog() =
        RestrictedApplicationDialogFragment().show(childFragmentManager, RestrictedApplicationDialogFragment.TAG)

    companion object {
        fun newInstance() = LauncherFragment()
    }
}
