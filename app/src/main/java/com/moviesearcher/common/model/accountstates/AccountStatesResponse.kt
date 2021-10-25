package com.moviesearcher.common.model.accountstates

data class AccountStatesResponse(
    val favorite: Boolean?,
    val id: Int?,
    val rated: Rated?,
    val watchlist: Boolean?
)