package com.example.shopify.checkout.presentation.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.shopify.checkout.domain.model.CartItem
import com.example.shopify.databinding.CartItemBinding
import com.example.shopify.settings.domain.model.AddressModel

class CartRecyclerAdapter(
    private val onDeleteClickAction: (Long) -> Unit
) : ListAdapter<CartItem, CartRecyclerAdapter.ViewHolder>(
    DayDiffCallback()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            CartItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CartRecyclerAdapter.ViewHolder, position: Int) {
        val cartItem = getItem(position)
        holder.bind(cartItem)
    }

    inner class ViewHolder(var binding: CartItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(cartItem : CartItem) {
            binding.apply {
                this.cartItem = cartItem
                this.deleteClickListener = {onDeleteClickAction.invoke(cartItem.itemId)}
            }
        }
    }
}


class DayDiffCallback : DiffUtil.ItemCallback<CartItem>() {
    override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
        return oldItem.itemId == newItem.itemId
    }

    override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
        return oldItem == newItem
    }
}