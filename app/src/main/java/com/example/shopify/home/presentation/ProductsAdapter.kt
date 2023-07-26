package com.example.shopify.home.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.shopify.R
import com.example.shopify.databinding.ProductListItemBinding
import com.example.shopify.home.domain.model.ProductModel

class ProductsAdapter(
    var currency: String = "EGP",
    var exchangeRate: Double = 1.0,
    private val selectProduct: (ProductModel) -> Unit
) : ListAdapter<ProductModel, ProductViewHolder>(ProductDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = DataBindingUtil.inflate<ProductListItemBinding>(
            LayoutInflater.from(parent.context), R.layout.product_list_item, parent, false
        )
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = getItem(position)
        holder.bind(product,exchangeRate,currency)

        holder.binding.root.setOnClickListener {
            selectProduct(product)
        }
    }
}

class ProductViewHolder(val binding: ProductListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(product: ProductModel, exchangeRate: Double, currency: String) {
        binding.product = product
        binding.exchangeRate = exchangeRate
        binding.currency = currency
        binding.executePendingBindings()
    }
}

class ProductDiffUtil : DiffUtil.ItemCallback<ProductModel>() {
    override fun areItemsTheSame(oldItem: ProductModel, newItem: ProductModel): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: ProductModel, newItem: ProductModel): Boolean {
        return oldItem == newItem
    }

}


