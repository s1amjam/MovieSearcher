package com.moviesearcher.common.model.auth

import com.google.gson.annotations.SerializedName

class RequestToken(@SerializedName("request_token") val requestToken: String)