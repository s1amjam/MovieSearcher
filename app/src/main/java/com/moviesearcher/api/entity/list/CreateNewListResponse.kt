package com.moviesearcher.api.entity.list

import com.google.gson.annotations.SerializedName

data class CreateNewListResponse(
    @SerializedName("list_id")
    val listId: Int?,
    @SerializedName("status_code")
    val statusCode: Int?,
    @SerializedName("status_message")
    val statusMessage: String?,
    @SerializedName("success")
    val success: Boolean?
)