package com.example.shopify.utils.diffutils

import androidx.recyclerview.widget.DiffUtil
import com.example.shopify.settings.domain.model.AddressModel

class AddressModelDiffUtil : DiffUtil.ItemCallback<AddressModel>() {


    override fun areItemsTheSame(oldItem: AddressModel, newItem: AddressModel): Boolean {
        return oldItem.latitude == newItem.latitude && oldItem.longitude == newItem.longitude
    }

    override fun areContentsTheSame(oldItem: AddressModel, newItem: AddressModel): Boolean {
        return oldItem == newItem
    }
}