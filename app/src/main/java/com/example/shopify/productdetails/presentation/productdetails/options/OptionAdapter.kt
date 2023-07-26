package com.example.shopify.productdetails.presentation.productdetails.options

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.shopify.databinding.ProductOptionsLayoutBinding
import com.example.shopify.productdetails.domain.model.details.OptionModel
import timber.log.Timber

class OptionAdapter(
) : ListAdapter<OptionModel, RecyclerView.ViewHolder>(OptionDiffUtil()) {
    var selectedOptions = mutableSetOf<String>()
    lateinit var adapter: ValuesAdapter

    inner class ViewHolder(var binding: ProductOptionsLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(item: OptionModel) {
            binding.optionOneTV.text = item.name
            setupAdapter()
            binding.adapter = adapter
            binding.optionOneRV.setRecycledViewPool(RecyclerView.RecycledViewPool())
            adapter.submitList(item.values)
        }

        private fun setupAdapter() {
            adapter =
                ValuesAdapter()
                { newSelectedOption ->

                    /*
                    *
                    * If selectedOption and new selected option are from the same Recycler View
                    * then selectedOption should be set to the new value
                    * else if they are not from the same Recycler view
                    * then newSelectedOption should be APPENDED to the selectedOption
                    *
                    * */
                    if (selectedOptions.size < currentList.size) {

                        selectedOptions.add(newSelectedOption)
                    } else {
                        selectedOptions.remove(selectedOptions.last())
                        selectedOptions.add(newSelectedOption)
                    }

                    Timber.i("$newSelectedOption selected")

                }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ProductOptionsLayoutBinding = ProductOptionsLayoutBinding.inflate(
            inflater, parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentOption = getItem(position)
        if (holder is ViewHolder)
            holder.bind(currentOption)
    }

    fun resetSelections() {

        adapter.clearSelection()

        selectedOptions.clear()
    }
}


class OptionDiffUtil : DiffUtil.ItemCallback<OptionModel>() {
    override fun areItemsTheSame(
        oldItem: OptionModel,
        newItem: OptionModel
    ): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(
        oldItem: OptionModel,
        newItem: OptionModel
    ): Boolean {
        return oldItem == newItem
    }

}
