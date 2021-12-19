package com.exaper.launcher.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.exaper.launcher.data.Launchable
import com.exaper.launcher.databinding.LaunchableBinding

class LaunchablesAdapter : RecyclerView.Adapter<LaunchablesAdapter.LaunchableViewHolder>() {
    var items = emptyList<Launchable>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        LaunchableViewHolder(LaunchableBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: LaunchableViewHolder, position: Int) = holder.bindTo(items[position])

    inner class LaunchableViewHolder(private val binding: LaunchableBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindTo(launchable: Launchable) = with(binding) {
            launchableIconText.text = launchable.name
            launchableIconText.setCompoundDrawablesRelativeWithIntrinsicBounds(null, launchable.icon, null, null)
        }
    }
}