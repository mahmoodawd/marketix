package com.example.shopify.productdetails.presentation.prodouctreviews

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.shopify.databinding.ReviewItemBinding
import com.example.shopify.productdetails.domain.model.reviews.ReviewModel

class ProductReviewsAdapter(
) : ListAdapter<ReviewModel, RecyclerView.ViewHolder>(ReviewDiffUtil()) {


    inner class ViewHolder(var binding: ReviewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ReviewModel) {

            binding.review = item

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ReviewItemBinding = ReviewItemBinding.inflate(
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


class ReviewDiffUtil : DiffUtil.ItemCallback<ReviewModel>() {
    override fun areItemsTheSame(
        oldItem: ReviewModel,
        newItem: ReviewModel
    ): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(
        oldItem: ReviewModel,
        newItem: ReviewModel
    ): Boolean {
        return oldItem == newItem
    }

}
