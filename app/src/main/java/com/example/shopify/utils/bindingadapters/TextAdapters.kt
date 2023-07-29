package com.example.shopify.utils.bindingadapters

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.shopify.utils.rounder.roundTo

@BindingAdapter(value = ["bind:price", "bind:currency"], requireAll = true)
fun priceWithCurrency(textView: TextView, price: String, currency: String) {
    textView.text = "$price $currency"
}


@BindingAdapter(value = ["bind:price2", "bind:currency2", "bind:rate2"], requireAll = true)
fun priceWithCurrency2(textView: TextView, price: String, currency: String, exchangeRate: Double) {
    textView.text = "${price.toDouble().times(exchangeRate).roundTo(2)} $currency"
}