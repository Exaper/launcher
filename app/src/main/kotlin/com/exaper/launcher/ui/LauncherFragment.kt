package com.exaper.launcher.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.exaper.launcher.R
import com.exaper.launcher.databinding.MainFragmentBinding
import com.exaper.launcher.viewmodel.LauncherViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LauncherFragment : Fragment(R.layout.main_fragment) {
    private lateinit var binding: MainFragmentBinding
    private val viewModel by viewModels<LauncherViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = LaunchablesAdapter()
        adapter.onItemClicked = {
            if (it.restricted) {
                showRestrictedApplicationDialog()
            } else {
                startActivity(it.launchIntent)
            }
        }

        binding = MainFragmentBinding.bind(view).apply {
            recyclerView.adapter = adapter
        }

        viewModel.launchables.observe(viewLifecycleOwner) { launchables ->
            adapter.items = launchables
        }
    }

    private fun showRestrictedApplicationDialog() {
        RestrictedApplicationDialogFragment().show(childFragmentManager, RestrictedApplicationDialogFragment.TAG)
    }

    companion object {
        fun newInstance() = LauncherFragment()
    }
}
