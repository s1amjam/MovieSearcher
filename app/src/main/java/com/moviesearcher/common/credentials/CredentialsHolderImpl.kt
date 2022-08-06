package com.moviesearcher.common.credentials

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CredentialsHolderImpl @Inject constructor(private val sharedPrefs: SharedPreferences) :
    CredentialsHolder {

    override fun getSessionId() = sharedPrefs.getString("sessionId", "").toString()

    override fun getAccountId() = sharedPrefs.getLong("accountId", -1)

    override fun putSessionId(sessionId: String) =
        sharedPrefs.edit().putString("sessionId", sessionId).apply()

    override fun putAccountId(accountId: Long) =
        sharedPrefs.edit().putLong("accountId", accountId).apply()
}