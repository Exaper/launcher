package com.exaper.launcher.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
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
        binding = MainFragmentBinding.bind(view)
        val adapter = LaunchablesAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), resources.getInteger(R.integer.launchable_columns))

        viewModel.launchables.observe(viewLifecycleOwner) { launchables ->
            adapter.items = launchables
        }
    }

    companion object {
        fun newInstance() = LauncherFragment()
    }
}
