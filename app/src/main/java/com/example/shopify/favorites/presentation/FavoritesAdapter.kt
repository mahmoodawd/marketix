package com.example.shopify.favorites.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.shopify.databinding.ProductItemBinding
import com.example.shopify.favorites.domain.model.FavoriteProductModel

class FavoritesAdapter(
    private val onItemClick: (String) -> Unit,
    private val onDeleteClick: (String, Int) -> Unit,

    ) : ListAdapter<FavoriteProductModel, RecyclerView.ViewHolder>(FavDiffUtil()) {


    inner class ViewHolder constructor(var binding: ProductItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: FavoriteProductModel) {

            binding.product = item
            binding.onItemClick =
                { onItemClick.invoke(item.id.toString()) }
            binding.onDeleteClick = {
                onDeleteClick.invoke(item.draftOrderId.toString(), adapterPosition)
                binding.executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ProductItemBinding = ProductItemBinding.inflate(
            inflater, parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentProduct = getItem(position)
        if (holder is ViewHolder)
            holder.bind(currentProduct)
    }
}


class FavDiffUtil : DiffUtil.ItemCallback<FavoriteProductModel>() {
    override fun areItemsTheSame(
        oldItem: FavoriteProductModel,
        newItem: FavoriteProductModel
    ): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(
        oldItem: FavoriteProductModel,
        newItem: FavoriteProductModel
    ): Boolean {
        return oldItem == newItem
    }

}
