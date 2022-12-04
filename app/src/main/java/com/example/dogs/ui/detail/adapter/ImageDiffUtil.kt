package com.example.dogs.ui.detail.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.dogs.data.disk.model.RoomImageData

class ImageDiffUtil : DiffUtil.ItemCallback<RoomImageData>() {
    override fun areItemsTheSame(oldItem: RoomImageData, newItem: RoomImageData): Boolean {
        return oldItem.url == newItem.url
    }

    override fun areContentsTheSame(oldItem: RoomImageData, newItem: RoomImageData): Boolean {
        return oldItem == newItem
    }
}