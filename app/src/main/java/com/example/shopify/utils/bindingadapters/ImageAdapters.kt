package com.example.shopify.utils.bindingadapters

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.shopify.R

@BindingAdapter("loadImage")
fun loadProductImage(image: ImageView, src: String?) {
    Glide.with(image.context)
        .load(src).placeholder(R.drawable.loading_img)
        .error(R.drawable.ic_broken_image)
        .into(image)
}