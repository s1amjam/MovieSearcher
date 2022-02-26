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

    private lateinit var favoriteViewModel: FavoriteViewModel

    private var isFavorite = true
    private lateinit var mediaInfo: MutableMap<String, Long>

    private val markAsFavoriteIcon = R.drawable.ic_round_star_outline_36
    private val removeFromFavoriteIcon = R.drawable.ic_round_star_filled_36

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
        encryptedSharedPrefs = EncryptedSharedPrefs.sharedPrefs(requireContext())
        sessionId = encryptedSharedPrefs.getString("sessionId", "").toString()
        accountId = encryptedSharedPrefs.getLong("accountId", 0L)
    }

    private fun getMediaInfo(): MutableMap<String, Long> {
        val movieId = requireArguments().getLong("movie_id")
        val tvId = requireArguments().getLong("tv_id")
        val emptyMediaId: Long = 0
        val mediaInfo: MutableMap<String, Long> = mutableMapOf()

        if (movieId == emptyMediaId) {
            mediaInfo["tv"] = tvId
        } else {
            mediaInfo["movie"] = movieId
        }

        return mediaInfo
    }

    fun checkFavorites(button: ImageButton) {
        if (sessionId.isNotBlank()) {
            setupViewModel()
            mediaInfo = getMediaInfo()
            val mediaId = mediaInfo.values.first()
            val mediaKey = mediaInfo.keys.first()

            if (mediaKey == "movie") {
                favoriteViewModel.getFavoriteMovie().observe(
                    viewLifecycleOwner
                ) { it ->
                    when (it.status) {
                        Status.SUCCESS -> {
                            it.data?.let { favoriteMovieItems ->
                                button.setImageResource(markAsFavoriteIcon)
                                favoriteMovieItems.results!!.forEach {
                                    if (it.id == mediaId) {
                                        isFavorite = false
                                        button.setImageResource(removeFromFavoriteIcon)
                                    }
                                }
                            }
                        }
                        Status.LOADING -> {
                        }
                        Status.ERROR -> {
                            Toast.makeText(
                                requireContext(),
                                ERROR_MESSAGE.format(it.message),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            } else {
                favoriteViewModel.getFavoriteTv().observe(
                    viewLifecycleOwner
                ) { it ->
                    when (it.status) {
                        Status.SUCCESS -> {
                            it.data?.let { favoriteMovieItems ->
                                button.setImageResource(markAsFavoriteIcon)
                                favoriteMovieItems.results!!.forEach {
                                    if (it.id == mediaId) {
                                        isFavorite = false
                                        button.setImageResource(removeFromFavoriteIcon)
                                    }
                                }
                            }
                        }
                        Status.LOADING -> {
                        }
                        Status.ERROR -> {
                            Toast.makeText(
                                requireContext(),
                                ERROR_MESSAGE.format(it.message),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
        }
    }

    fun markAsFavorite(button: ImageButton) {
        mediaInfo = getMediaInfo()

        val markAsFavorite = Api.markAsFavorite(
            accountId,
            sessionId,
            MarkAsFavoriteRequest(isFavorite, mediaInfo.values.first(), mediaInfo.keys.first())
        )

        if (isFavorite) {
            button.setImageResource(removeFromFavoriteIcon)
            Toast.makeText(requireContext(), "Added to Favorites", Toast.LENGTH_SHORT).show()
        } else {
            button.setImageResource(markAsFavoriteIcon)
            Toast.makeText(requireContext(), "Removed from Favorites", Toast.LENGTH_SHORT)
                .show()
        }

        markAsFavorite.observe(viewLifecycleOwner) {
            if (it.statusCode == 13 || it.statusCode == 1 || it.statusCode == 12) {
                isFavorite = !isFavorite
            } else {
                Toast.makeText(
                    requireContext(),
                    "Error adding to Favorites. Try again later.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun hideKeyboard(view: View) {
        val inputMethodService =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodService.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun setupViewModel() {
        if (sessionId.isNotEmpty()) {
            favoriteViewModel = ViewModelProvider(
                this,
                ViewModelFactory(
                    sessionId, accountId
                )
            ).get(FavoriteViewModel::class.java)
        }
    }
}