package com.example.dogs.ui.detail.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dogs.R
import com.example.dogs.data.disk.model.RoomImageData
import com.example.dogs.databinding.ListItemImageBinding

class ImageAdapter(val listener: Listener) : ListAdapter<RoomImageData, ImageAdapter.ViewHolder>(
    ImageDiffUtil()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(holder.itemView.context, getItem(position))
    }

    inner class ViewHolder(private val binding: ListItemImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(context: Context, item: RoomImageData) {

            binding.starButton.setImageResource( if(item.isFavorite) R.drawable.ic_star else R.drawable.ic_star_empty)
            binding.starButton.setOnClickListener {
                listener.onItemFavoriteClicked(item.url, !item.isFavorite)
            }

            binding.tv.text = item.breedId
            binding.tv.setOnClickListener {
                listener.onItemTextClicked(item.breedId)
            }

            Glide
                .with(context)
                .load(item.url)
                .centerCrop()
                .placeholder(R.drawable.place_holder)
                .into(binding.iv)
        }
    }

    interface Listener {
        fun onItemFavoriteClicked(id: String, newIsFavorite: Boolean)
        fun onItemTextClicked(breedId: String)
    }
}