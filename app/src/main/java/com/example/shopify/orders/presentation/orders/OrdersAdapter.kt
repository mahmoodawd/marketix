package com.example.shopify.orders.presentation.orders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.shopify.R
import com.example.shopify.databinding.OrderListItemBinding
import com.example.shopify.orders.domain.model.LineItemModel
import com.example.shopify.orders.domain.model.OrderModel

class OrdersAdapter(private val gotToOrderDetails: (OrderModel) -> Unit) :
    ListAdapter<OrderModel, OrderViewHolder>(OrderDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = DataBindingUtil.inflate<OrderListItemBinding>(
            LayoutInflater.from(parent.context), R.layout.order_list_item, parent, false
        )
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = getItem(position)
        holder.binding.root.setOnClickListener {
            gotToOrderDetails(order)
        }
        holder.bind(order)
    }
}

class OrderViewHolder(val binding: OrderListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(order: OrderModel) {
        binding.order = order
        binding.executePendingBindings()
    }
}

class OrderDiffUtil : DiffUtil.ItemCallback<OrderModel>() {
    override fun areItemsTheSame(oldItem: OrderModel, newItem: OrderModel): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: OrderModel, newItem: OrderModel): Boolean {
        return oldItem == newItem
    }

}
