package com.moviesearcher.common

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.moviesearcher.R
import com.moviesearcher.api.Api
import com.moviesearcher.common.utils.EncryptedSharedPrefs
import com.moviesearcher.common.utils.Status
import com.moviesearcher.common.viewmodel.ViewModelFactory
import com.moviesearcher.favorite.FavoriteViewModel
import com.moviesearcher.favorite.model.MarkAsFavoriteRequest
import kotlin.properties.Delegates

private const val TAG = "BaseFragment"

open class BaseFragment : Fragment() {
    val ERROR_MESSAGE = "Something went wrong '%s'"
    lateinit var sessionId: String
    var accountId by Delegates.notNull<Long>()

    lateinit var encryptedSharedPrefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
        encryptedSharedPrefs = EncryptedSharedPrefs.sharedPrefs(requireContext())
        sessionId = encryptedSharedPrefs.getString("sessionId", "").toString()
        accountId = encryptedSharedPrefs.getLong("accountId", 0L)
    }

    fun hideKeyboard(view: View) {
        val inputMethodService =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodService.hideSoftInputFromWindow(view.windowToken, 0)
    }
}