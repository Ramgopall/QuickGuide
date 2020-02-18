package com.triviallanguages.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("image")
fun loadImage(view: ImageView, url: Int) {
    Glide.with(view.context).load(url).into(view)
}


@BindingAdapter(value = ["roundImageFromUrl"],requireAll = false)
fun loadRoundImageFromUrl(view: ImageView, url: String?) {
    val circularProgressDrawable = CircularProgressDrawable(view.context)
    circularProgressDrawable.strokeWidth = 5f
    circularProgressDrawable.centerRadius = 30f
    circularProgressDrawable.start()

    Glide.with(view.context)
        .load(url)
        .apply(
            RequestOptions()
                .circleCrop()
                .placeholder(circularProgressDrawable)
        )
        .into(view)
}