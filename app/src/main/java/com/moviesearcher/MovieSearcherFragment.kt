package com.moviesearcher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.moviesearcher.Constants.IMAGE_URL
import com.moviesearcher.entity.Result
import com.moviesearcher.entity.TrendingResponse
import com.squareup.picasso.Picasso

class MovieSearcherFragment : Fragment() {
    private lateinit var movieRecyclerView: RecyclerView
    private lateinit var movieViewModel: MovieViewModel
    private lateinit var tvViewModel: TvViewModel
    private lateinit var trendingMovieButton: Button
    private lateinit var trendingTvButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_movie_searcher, container, false)

        movieRecyclerView = view.findViewById(R.id.movie_recycler_view)
        trendingMovieButton = view.findViewById(R.id.trending_movie_button)
        trendingTvButton = view.findViewById(R.id.trending_tv_button)
        movieRecyclerView.layoutManager = GridLayoutManager(context, 3)
        movieViewModel = ViewModelProvider(this).get(MovieViewModel::class.java)
        tvViewModel = ViewModelProvider(this).get(TvViewModel::class.java)

        trendingTvButton.setOnClickListener {
            tvViewModel.tvItemLiveData.observe(
                viewLifecycleOwner,
                { movieItems ->
                    movieRecyclerView.adapter = MovieAdapter(movieItems)
                })
        }

        trendingMovieButton.setOnClickListener {
            movieViewModel.movieItemLiveData.observe(
                viewLifecycleOwner,
                { movieItems ->
                    movieRecyclerView.adapter = MovieAdapter(movieItems)
                })
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieViewModel.movieItemLiveData.observe(
            viewLifecycleOwner,
            { movieItems ->
                movieRecyclerView.adapter = MovieAdapter(movieItems)
            })

        movieRecyclerView.addItemDecoration(
            GridSpacingItemDecoration(
                3,
                5,
                true
            )
        )
    }

    private class MovieHolder(private val itemImageView: ImageView) :
        RecyclerView.ViewHolder(itemImageView) {

        fun bindMovieItem(movieItem: Result) {
            Picasso.get()
                .load(IMAGE_URL + movieItem.posterPath)
                .into(itemImageView)
        }
    }

    private inner class MovieAdapter(private val movieItems: TrendingResponse) :
        RecyclerView.Adapter<MovieHolder>() {

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): MovieHolder {
            val view = layoutInflater.inflate(
                R.layout.list_item_poster,
                parent,
                false
            ) as ImageView
            return MovieHolder(view)
        }

        override fun getItemCount(): Int = movieItems.results?.size!!
        override fun onBindViewHolder(holder: MovieHolder, position: Int) {
            val movieItem = movieItems.results?.get(position)
            holder.bindMovieItem(movieItem!!)
        }
    }

    class GridSpacingItemDecoration(
        private val spanCount: Int,
        private val spacing: Int,
        private val includeEdge: Boolean
    ) : ItemDecoration() {
        override fun getItemOffsets(
            outRect: android.graphics.Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val position = parent.getChildAdapterPosition(view)
            val column = position % spanCount
            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount
                outRect.right = (column + 1) * spacing / spanCount
                if (position < spanCount) {
                    outRect.top = spacing
                }
                outRect.bottom = spacing
            } else {
                outRect.left = column * spacing / spanCount
                outRect.right = spacing - (column + 1) * spacing / spanCount
                if (position >= spanCount) {
                    outRect.top = spacing
                }
            }
        }
    }

    companion object {
        fun newInstance() = MovieSearcherFragment()
    }
}