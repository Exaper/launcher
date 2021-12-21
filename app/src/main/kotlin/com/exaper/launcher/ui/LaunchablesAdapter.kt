package com.exaper.launcher.ui

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
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

    var onItemClicked: (Launchable) -> (Unit) = {}

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        LaunchableViewHolder(LaunchableBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: LaunchableViewHolder, position: Int) = holder.bindTo(items[position])

    inner class LaunchableViewHolder(private val binding: LaunchableBinding) : RecyclerView.ViewHolder(binding.root) {
        private val grayscaleFilter = ColorMatrixColorFilter(ColorMatrix().apply { setSaturation(0f) })

        fun bindTo(launchable: Launchable) = with(binding) {
            iconImageView.setImageDrawable(launchable.icon)
            if (launchable.restricted) {
                iconImageView.colorFilter = grayscaleFilter
                iconImageView.imageAlpha = 128
            } else {
                iconImageView.colorFilter = null
                iconImageView.imageAlpha = 255
            }
            labelTextView.text = launchable.name
            root.setOnClickListener { onItemClicked(launchable) }
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
            return oldList[oldItemPosition] == newList[newItemPosition]
//            oldItem.name == newItem.name && oldItem.icon == newItem.icon && oldItem.launchIntent == newItem
//                    .launchIntent
//                    && oldItem.restricted == newItem.restricted
        }
    }


}