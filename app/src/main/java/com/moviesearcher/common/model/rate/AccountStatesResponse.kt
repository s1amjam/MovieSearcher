package com.moviesearcher.common.model.rate

import com.google.gson.annotations.SerializedName

data class AccountStatesResponse(
    @SerializedName("favorite")
    val favorite: Boolean? = null,
    @SerializedName("id")
    val id: Long? = null,
    @SerializedName("rated")
    val rated: Rated? = null,
    @SerializedName("watchlist")
    val watchlist: Boolean? = null
)