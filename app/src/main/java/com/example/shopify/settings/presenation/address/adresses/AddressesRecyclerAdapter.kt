package com.example.shopify.settings.presenation.address.adresses

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.shopify.databinding.AddressItemBinding
import com.example.shopify.settings.domain.model.AddressModel

class AddressesRecyclerAdapter(
    private val onClickAction: (AddressModel) -> Unit
) : ListAdapter<AddressModel, AddressesRecyclerAdapter.ViewHolder>(
    DayDiffCallback()
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


class DayDiffCallback : DiffUtil.ItemCallback<AddressModel>() {
    override fun areItemsTheSame(oldItem: AddressModel, newItem: AddressModel): Boolean {
        return oldItem.latitude == newItem.latitude && oldItem.longitude == newItem.longitude
    }

    override fun areContentsTheSame(oldItem: AddressModel, newItem: AddressModel): Boolean {
        return oldItem == newItem
    }
}