package com.example.dogs.ui.list.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.dogs.data.disk.model.RoomBreedData

class BreedDiffUtil : DiffUtil.ItemCallback<RoomBreedData>() {
    override fun areItemsTheSame(oldItem: RoomBreedData, newItem: RoomBreedData): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: RoomBreedData, newItem: RoomBreedData): Boolean {
        return oldItem == newItem
    }
}