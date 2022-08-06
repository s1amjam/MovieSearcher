package com.moviesearcher.common.credentials


interface CredentialsHolder {

    fun getSessionId(): String

    fun getAccountId(): Long

    fun putSessionId(sessionId: String)

    fun putAccountId(accountId: Long)
}