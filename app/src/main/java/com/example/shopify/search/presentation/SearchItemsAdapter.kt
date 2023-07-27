package com.example.shopify.search.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.shopify.databinding.SearchResultItemBinding
import com.example.shopify.search.domain.model.SearchProductModel

class SearchItemsAdapter(
    private val onItemClick: (String) -> Unit
) : ListAdapter<SearchProductModel, RecyclerView.ViewHolder>(SearchDiffUtil()) {


    inner class ViewHolder(var binding: SearchResultItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SearchProductModel) {

            binding.product = item
            binding.onItemClick = { onItemClick.invoke(item.id) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: SearchResultItemBinding = SearchResultItemBinding.inflate(
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


class SearchDiffUtil : DiffUtil.ItemCallback<SearchProductModel>() {
    override fun areItemsTheSame(
        oldItem: SearchProductModel,
        newItem: SearchProductModel
    ): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(
        oldItem: SearchProductModel,
        newItem: SearchProductModel
    ): Boolean {
        return oldItem == newItem
    }

}
