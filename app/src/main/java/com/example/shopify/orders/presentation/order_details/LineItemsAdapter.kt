package com.example.shopify.orders.presentation.order_details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.shopify.R
import com.example.shopify.databinding.OrderProductItemBinding
import com.example.shopify.orders.domain.model.LineItemModel

class LineItemsAdapter(
    var currency: String = "EGP",
    var exchangeRate: Double = 1.0, private val goToProduct: (productId: Long) -> Unit
) :
    ListAdapter<LineItemModel, LineItemsViewHolder>(LineItemDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LineItemsViewHolder {
        val binding = DataBindingUtil.inflate<OrderProductItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.order_product_item,
            parent,
            false
        )
        return LineItemsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LineItemsViewHolder, position: Int) {
        val lineItem = getItem(position)
        holder.binding.root.setOnClickListener {
            goToProduct(lineItem.productId)
        }
        holder.bind(lineItem, currency, exchangeRate)
    }

}

class LineItemsViewHolder(val binding: OrderProductItemBinding) : ViewHolder(binding.root) {
    fun bind(lineItem: LineItemModel, currency: String, exchangeRate: Double) {
        binding.lineItem = lineItem
        binding.currency = currency
        binding.exchangeRate = exchangeRate
        binding.executePendingBindings()
    }
}

class LineItemDiffUtil : DiffUtil.ItemCallback<LineItemModel>() {
    override fun areItemsTheSame(oldItem: LineItemModel, newItem: LineItemModel): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: LineItemModel, newItem: LineItemModel): Boolean {
        return oldItem == newItem
    }

}
