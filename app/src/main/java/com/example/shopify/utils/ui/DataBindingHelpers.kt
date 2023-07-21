package com.example.shopify.utils.ui

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso

@BindingAdapter(value = ["imageUrl", "placeholder"], requireAll = false)
fun setImageUrl(imageView: ImageView, url: String?, placeHolder: Drawable?) {
    if (url == null) {
        imageView.setImageDrawable(placeHolder);
    } else {
        Picasso.get().load(url).into(imageView)
    }
}