package com.moviesearcher.list.model

import com.google.gson.annotations.SerializedName

data class ListResponse(
    @SerializedName("created_by")
    val createdBy: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("favorite_count")
    val favoriteCount: Int?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("iso_639_1")
    val iso6391: String?,
    @SerializedName("item_count")
    val itemCount: Int?,
    @SerializedName("items")
    val items: MutableList<Item>?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("poster_path")
    val posterPath: String?
)