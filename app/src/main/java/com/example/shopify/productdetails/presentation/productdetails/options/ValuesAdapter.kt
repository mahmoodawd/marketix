package com.example.shopify.productdetails.presentation.productdetails.options

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.shopify.databinding.ClickedProductOptionItemBinding
import com.example.shopify.databinding.ProductOptionItemBinding

class ValuesAdapter(
    private val selectItem: (String) -> Unit
) : ListAdapter<String, RecyclerView.ViewHolder>(ValueDiffUtil()) {
    private var selectedItemPosition = -1

    override fun getItemViewType(position: Int): Int {
        return if (position == selectedItemPosition) 1 else 0
    }


    inner class ViewHolderNonClicked(var binding: ProductOptionItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: String) {
            binding.valueTv.text = item

        }
    }

    inner class ViewHolderClicked(var binding: ClickedProductOptionItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: String) {
            binding.valueTv.text = item
            binding.root.setOnClickListener {
                selectItem(item)
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val clickedItemBinding: ClickedProductOptionItemBinding =
            ClickedProductOptionItemBinding.inflate(
                inflater, parent, false
            )

        val nonClickedItemBinding: ProductOptionItemBinding = ProductOptionItemBinding.inflate(
            inflater, parent, false
        )
        return when (viewType) {
            1 -> {
                ViewHolderClicked(clickedItemBinding)
            }

            else -> {
                ViewHolderNonClicked(nonClickedItemBinding)

            }
        }


    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = getItem(position)
        when (holder) {
            is ViewHolderClicked -> {
                holder.bind(currentItem)
                holder.binding.root.isSelected = selectedItemPosition == position
            }

            is ViewHolderNonClicked -> {
                holder.bind(currentItem)
                holder.binding.root.setOnClickListener {
                    selectItem(currentItem)
                    val previouslySelectedItemPosition = selectedItemPosition
                    selectedItemPosition = holder.adapterPosition
                    notifyItemChanged(previouslySelectedItemPosition)
                    notifyItemChanged(position)
                }
                holder.binding.root.isSelected = selectedItemPosition == position

            }
        }

    }

    fun clearSelection() {
        val previouslySelectedItemPosition = selectedItemPosition
        selectedItemPosition = -1
        notifyItemChanged(previouslySelectedItemPosition)
    }
}


class ValueDiffUtil : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(
        oldItem: String,
        newItem: String
    ): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(
        oldItem: String,
        newItem: String
    ): Boolean {
        return oldItem == newItem
    }

}
