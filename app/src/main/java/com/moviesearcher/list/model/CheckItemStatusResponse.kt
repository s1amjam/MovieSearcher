package com.moviesearcher.list.model

import com.google.gson.annotations.SerializedName

data class CheckItemStatusResponse(
    @SerializedName("id")
    val id: String?,
    @SerializedName("item_present")
    val itemPresent: Boolean?
)