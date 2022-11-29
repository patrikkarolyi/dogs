package com.example.dogs.ui.detail.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dogs.R
import com.example.dogs.databinding.ListItemImageBinding

class ImageAdapter : ListAdapter<String, ImageAdapter.ViewHolder>(
    ImageDiffUtil()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ListItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(holder.itemView.context, getItem(position))
    }

    inner class ViewHolder(private val binding: ListItemImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(context: Context, item: String) {
            Glide
                .with(context)
                .load(item)
                .centerCrop()
                .placeholder(R.drawable.place_holder)
                .into(binding.iv)
        }
    }
}