package com.example.shopify.utils.diffutils

import androidx.recyclerview.widget.DiffUtil
import com.example.shopify.home.domain.model.discountcode.DiscountCodeModel

class DiscountCodeDiffUtil : DiffUtil.ItemCallback<DiscountCodeModel>() {

    override fun areItemsTheSame(oldItem: DiscountCodeModel, newItem: DiscountCodeModel): Boolean {
        return oldItem.id == newItem.id && oldItem.code == newItem.code
    }

    override fun areContentsTheSame(oldItem: DiscountCodeModel, newItem: DiscountCodeModel): Boolean {
        return oldItem == newItem
    }
}