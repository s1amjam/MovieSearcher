package com.moviesearcher.list.lists.model

import com.google.gson.annotations.SerializedName
import com.moviesearcher.list.model.Result

data class ListsResponse(
    @SerializedName("page")
    val page: Int?,
    @SerializedName("results")
    val results: MutableList<Result>?,
    @SerializedName("total_pages")
    val totalPages: Int?,
    @SerializedName("total_results")
    val totalResults: Int?
)