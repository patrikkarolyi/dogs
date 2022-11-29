package com.example.dogs.ui.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dogs.data.disk.model.RoomBreedData
import com.example.dogs.databinding.ListItemBreedBinding

class BreedAdapter(val listener: Listener) : ListAdapter<RoomBreedData, BreedAdapter.ViewHolder>(
    BreedDiffUtil()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemBreedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ListItemBreedBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RoomBreedData) {
            val name =
                if (item.subBreedName.isBlank()) item.breedName else "${item.subBreedName} ${item.breedName}"
            binding.tv.text = name
            binding.root.setOnClickListener {
                listener.onItemSelected(item.id)
            }
        }
    }

    interface Listener {
        fun onItemSelected(id: String)
    }
}