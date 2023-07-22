package com.example.shopify.utils.bindingadapters

import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter(value = ["bind:price", "bind:currency"],requireAll = true)
fun priceWithCurrency(textView: TextView, price : String , currency : String) {
    textView.text = "$price $currency"
}