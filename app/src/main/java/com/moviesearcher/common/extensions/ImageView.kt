package com.moviesearcher.common.extensions

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.moviesearcher.R

fun ImageView.loadImage(uri: String, isYouTube: Boolean = false, isCardView: Boolean = false) {
    Glide.with(this)
        .load(uri)
        .placeholder(R.drawable.ic_placeholder)
        .centerCrop().apply {
            if (isYouTube) {
                override(800, 600)
            } else if (isCardView) {
                override(400, 600)
            } else {
                override(300, 500)
            }
        }.into(this@loadImage)
}