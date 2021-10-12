package com.moviesearcher.common

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageButton
import androidx.annotation.MenuRes
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moviesearcher.R
import com.moviesearcher.api.Api
import com.moviesearcher.common.model.common.MediaId
import com.moviesearcher.favorite.common.model.MarkAsFavoriteRequest
import com.moviesearcher.list.CreateNewListDialog
import com.moviesearcher.list.lists.viewmodel.MyListsViewModel
import com.moviesearcher.list.model.Result
import com.moviesearcher.utils.EncryptedSharedPrefs
import com.moviesearcher.watchlist.common.model.WatchlistRequest
import kotlin.properties.Delegates

private const val TAG = "BaseFragment"

open class BaseFragment : Fragment() {
    lateinit var sessionId: String
    var accountId by Delegates.notNull<Long>()

    lateinit var encryptedSharedPrefs: SharedPreferences

    private val myListsViewModel: MyListsViewModel by viewModels()

    private var isFavorite = false
    private var isWatchlist = false
    private lateinit var mediaInfo: MutableMap<String, Long>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
        encryptedSharedPrefs = EncryptedSharedPrefs.sharedPrefs(requireContext())
        sessionId = encryptedSharedPrefs.getString("sessionId", "").toString()
        accountId = encryptedSharedPrefs.getLong("accountId", 0L)
    }

    private fun showCreateNewListDialog() {
        val dialog = CreateNewListDialog()
        dialog.show(childFragmentManager, "CreateNewListDialogFragment")
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

    fun showAddToListMenu(v: View, @MenuRes menuRes: Int, resultList: MutableList<Result>) {
        val mediaId = getMediaInfo().values.first()
        val popup = PopupMenu(requireContext(), v)

        popup.menuInflater.inflate(menuRes, popup.menu)

        popup.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_item_create_new_list -> {
                    showCreateNewListDialog()
                }
            }
            true
        }

        resultList.forEach { it ->
            popup.menu.add(Menu.NONE, it.id!!.toInt(), Menu.NONE, it.name)
            val menuItem = popup.menu.findItem(it.id.toInt())

            myListsViewModel.checkItemStatus(it.id.toInt(), mediaId).observe(viewLifecycleOwner,
                { item ->
                    if (item.itemPresent == true) {
                        menuItem.isEnabled = false
                        menuItem.title = menuItem.title.toString() + " (added)"
                    } else {
                        menuItem.setOnMenuItemClickListener {
                            Api.addToList(it.itemId, MediaId(mediaId), sessionId)

                            true
                        }
                    }
                })
        }
        popup.show()
    }

    fun checkFavorites(button: Button) {
        if (sessionId.isNotBlank()) {
            mediaInfo = getMediaInfo()
            val mediaId = mediaInfo.values.first()
            val mediaKey = mediaInfo.keys.first()
            val favoriteMovies = Api.getFavoriteMovies(accountId, sessionId)
            val favoriteTvs = Api.getFavoriteTvs(accountId, sessionId)
            val markAsFavoriteText = resources.getText(R.string.mark_as_favorite)
            val removeFromFavoriteText = resources.getText(R.string.remove_from_favorite)

            if (mediaKey == "movie") {
                favoriteMovies.observe(viewLifecycleOwner, { favoriteItem ->
                    button.text = markAsFavoriteText
                    favoriteItem.results!!.forEach {
                        if (it.id == mediaId) {
                            isFavorite = false
                            button.text = removeFromFavoriteText
                        }
                    }
                })
            } else {
                favoriteTvs.observe(viewLifecycleOwner, { favoriteItem ->
                    button.text = markAsFavoriteText
                    favoriteItem.results!!.forEach {
                        if (it.id == mediaId) {
                            isFavorite = false
                            button.text = removeFromFavoriteText
                        }
                    }
                })
            }
        }
    }

    fun markAsFavorite(button: Button) {
        mediaInfo = getMediaInfo()

        val markAsFavorite = Api.markAsFavorite(
            accountId,
            sessionId,
            MarkAsFavoriteRequest(isFavorite, mediaInfo.values.first(), mediaInfo.keys.first())
        )

        markAsFavorite.observe(viewLifecycleOwner, {
            if (it.statusCode == 13 || it.statusCode == 1 || it.statusCode == 12) {
                isFavorite = true
                checkFavorites(button)
            }
        })
    }

    fun checkWatchlist(button: ImageButton) {
        if (sessionId.isNotBlank()) {
            mediaInfo = getMediaInfo()
            val mediaId = mediaInfo.values.first()
            val mediaKey = mediaInfo.keys.first()
            val moviesWatchlist = Api.getMovieWatchlist(accountId, sessionId)
            val tvsWatchlist = Api.getTvWatchlist(accountId, sessionId)

            if (mediaKey == "movie") {
                moviesWatchlist.observe(viewLifecycleOwner, { item ->
                    button.setImageResource(R.drawable.ic_baseline_bookmark_add_60)
                    item.results!!.forEach {
                        if (it.id == mediaId) {
                            isWatchlist = false
                            button.setImageResource(R.drawable.ic_baseline_bookmark_added_60)
                        }
                    }
                })
            } else {
                tvsWatchlist.observe(viewLifecycleOwner, { item ->
                    button.setImageResource(R.drawable.ic_baseline_bookmark_add_60)
                    item.results!!.forEach {
                        if (it.id == mediaId) {
                            isWatchlist = false
                            button.setImageResource(R.drawable.ic_baseline_bookmark_added_60)
                        }
                    }
                })
            }
        }
    }

    fun addToWatchlist(button: ImageButton) {
        mediaInfo = getMediaInfo()

        val addToWatchlist = Api.watchlist(
            accountId,
            sessionId,
            WatchlistRequest(isWatchlist, mediaInfo.values.first(), mediaInfo.keys.first())
        )

        addToWatchlist.observe(viewLifecycleOwner, {
            if (it.statusCode == 13 || it.statusCode == 1 || it.statusCode == 12) {
                isWatchlist = true
                checkWatchlist(button)
            }
        })
    }

    open fun setupUi(
        _adapter: RecyclerView.Adapter<*>,
        recyclerView: RecyclerView
    ) {
        setupRecyclerView(_adapter, recyclerView)
    }

    open fun setupRecyclerView(
        _adapter: RecyclerView.Adapter<*>,
        recyclerView: RecyclerView
    ) {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            adapter = _adapter
            setHasFixedSize(true)
        }
    }

    fun hideKeyboard(view: View) {
        val inputMethodService =
            context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        inputMethodService.hideSoftInputFromWindow(view.windowToken, 0)
    }
}