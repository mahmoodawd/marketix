package com.example.shopify.home.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopify.R
import com.example.shopify.databinding.BrandListItemBinding
import com.example.shopify.databinding.ClickedBrandItemBinding
import com.example.shopify.home.domain.model.BrandModel

class BrandsAdapter(private val context: Context, private val selectBrand: (BrandModel) -> Unit) :
    ListAdapter<BrandModel, RecyclerView.ViewHolder>(BrandsDiffUtil()) {
    private var selectedItemPosition = -1

    override fun getItemViewType(position: Int): Int {
        return if (position == selectedItemPosition) 1 else 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            1 -> {
                val bindingSelected = ClickedBrandItemBinding.inflate(inflater, parent, false)
                BrandViewHolderClicked(bindingSelected)
            }

            else -> {
                val bindingNotSelected = BrandListItemBinding.inflate(inflater, parent, false)
                BrandViewHolderNonClicked(bindingNotSelected)
            }
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val brandItem = getItem(position)
        when (holder) {
            is BrandViewHolderNonClicked -> {
                Glide.with(context)
                    .load(brandItem.image.src)
                    .placeholder(R.drawable.loading_img)
                    .error(R.drawable.ic_broken_image)
                    .into(holder.nonClickedBinding.brandImage)
                holder.nonClickedBinding.root.setOnClickListener {
                    selectBrand(brandItem)
                    val previouslySelectedItemPosition = selectedItemPosition
                    selectedItemPosition = holder.adapterPosition
//                    brandItem.clicked = true
                    notifyItemChanged(previouslySelectedItemPosition)
                    notifyItemChanged(position)
                }
                holder.nonClickedBinding.root.isSelected = selectedItemPosition == position
            }

            is BrandViewHolderClicked -> {
                holder.clickedBinding.brandNameTv.text = brandItem.title
                Glide.with(context)
                    .load(brandItem.image.src)
                    .placeholder(R.drawable.loading_img)
                    .error(R.drawable.ic_broken_image)
                    .into(holder.clickedBinding.brandImage)
                holder.clickedBinding.root.setOnClickListener {
                    selectBrand(brandItem)
                }
                holder.clickedBinding.root.isSelected = selectedItemPosition == position
            }
        }

    }
}

class BrandViewHolderNonClicked(val nonClickedBinding: BrandListItemBinding) :
    RecyclerView.ViewHolder(nonClickedBinding.root)

class BrandViewHolderClicked(val clickedBinding: ClickedBrandItemBinding) :
    RecyclerView.ViewHolder(clickedBinding.root)

class BrandsDiffUtil : DiffUtil.ItemCallback<BrandModel>() {
    override fun areItemsTheSame(oldItem: BrandModel, newItem: BrandModel): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: BrandModel, newItem: BrandModel): Boolean {
        return oldItem == newItem
    }

}
