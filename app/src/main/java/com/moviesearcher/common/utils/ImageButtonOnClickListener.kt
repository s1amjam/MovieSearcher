package com.moviesearcher.common.utils

import android.widget.ImageButton

class OnClickListener(
    val clickListener: (
        button: ImageButton,
        mediaInfo: MutableMap<String, Long>?
    ) -> Unit
) {
    fun onClick(button: ImageButton, mediaInfo: MutableMap<String, Long>? = mutableMapOf()) =
        clickListener(button, mediaInfo)
}