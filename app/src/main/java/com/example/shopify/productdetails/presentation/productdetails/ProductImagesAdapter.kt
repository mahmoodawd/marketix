package com.example.shopify.productdetails.presentation.productdetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.shopify.databinding.ProductImageItemBinding
import com.example.shopify.productdetails.domain.model.ImageModel

class ProductImagesAdapter(
) : ListAdapter<ImageModel, RecyclerView.ViewHolder>(ImgDiffUtil()) {


    inner class ViewHolder(var binding: ProductImageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ImageModel) {

            binding.image = item
            binding.textView2.text = item.src
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ProductImageItemBinding = ProductImageItemBinding.inflate(
            inflater, parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentImage = getItem(position)
        if (holder is ViewHolder)
            holder.bind(currentImage)
    }
}


class ImgDiffUtil : DiffUtil.ItemCallback<ImageModel>() {
    override fun areItemsTheSame(
        oldItem: ImageModel,
        newItem: ImageModel
    ): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(
        oldItem: ImageModel,
        newItem: ImageModel
    ): Boolean {
        return oldItem == newItem
    }

}
