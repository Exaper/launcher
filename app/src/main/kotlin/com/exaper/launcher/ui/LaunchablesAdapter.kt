package com.exaper.launcher.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.exaper.launcher.data.Launchable
import com.exaper.launcher.databinding.LaunchableBinding

class LaunchablesAdapter : RecyclerView.Adapter<LaunchablesAdapter.LaunchableViewHolder>() {
    var items = emptyList<Launchable>()
        set(value) {
            DiffUtil.calculateDiff(DiffCallback(items, value)).dispatchUpdatesTo(this)
            field = value
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

    inner class DiffCallback(private val oldList: List<Launchable>, private val newList: List<Launchable>) : DiffUtil.Callback() {
        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition].pkg == newList[newItemPosition].pkg

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return oldItem.name == newItem.name && oldItem.icon == newItem.icon && oldItem.launchIntent == newItem.launchIntent
        }
    }


}