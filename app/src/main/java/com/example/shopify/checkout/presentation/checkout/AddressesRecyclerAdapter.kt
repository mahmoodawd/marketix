package com.example.shopify.checkout.presentation.checkout

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.shopify.databinding.AddressItemBinding
import com.example.shopify.settings.domain.model.AddressModel
import com.example.shopify.utils.diffutils.AddressModelDiffUtil

class AddressesRecyclerAdapter(
    private val onClickAction: (AddressModel) -> Unit
) : ListAdapter<AddressModel, AddressesRecyclerAdapter.ViewHolder>(
    AddressModelDiffUtil()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            AddressItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: AddressesRecyclerAdapter.ViewHolder, position: Int) {
        val location = getItem(position)
        holder.bind(location)
    }

    inner class ViewHolder(var binding: AddressItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(address: AddressModel) {
            binding.apply {
                this.address = address
                this.addressClickListener = {onClickAction(address)}
            }
        }
    }
}

