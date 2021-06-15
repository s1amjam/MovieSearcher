package com.moviesearcher.api.entity.auth

import com.google.gson.annotations.SerializedName

class RequestToken(requestToken: String) {
    @SerializedName("request_token")
    val requestToken: String = requestToken
}