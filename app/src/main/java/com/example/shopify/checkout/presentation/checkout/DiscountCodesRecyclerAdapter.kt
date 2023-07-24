package com.example.shopify.checkout.presentation.checkout

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.shopify.databinding.CodeItemBinding
import com.example.shopify.home.domain.model.discountcode.DiscountCodeModel
import com.example.shopify.utils.diffutils.DiscountCodeDiffUtil

class DiscountCodesRecyclerAdapter(
    private val onClickAction: (DiscountCodeModel) -> Unit
) : ListAdapter<DiscountCodeModel, DiscountCodesRecyclerAdapter.ViewHolder>(
    DiscountCodeDiffUtil()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            CodeItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: DiscountCodesRecyclerAdapter.ViewHolder, position: Int) {
        val location = getItem(position)
        holder.bind(location)
    }

    inner class ViewHolder(var binding: CodeItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(code: DiscountCodeModel) {
            binding.apply {
                this.code = code
                this.discountCodeClickListener = {onClickAction(code)}
            }
        }
    }
}