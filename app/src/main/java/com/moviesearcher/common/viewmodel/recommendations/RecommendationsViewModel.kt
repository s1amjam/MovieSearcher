package com.moviesearcher.common.viewmodel.recommendations

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.moviesearcher.api.Api
import com.moviesearcher.favorite.movie.model.FavoriteMovieResponse
import com.moviesearcher.favorite.tv.model.FavoriteTvResponse

class RecommendationsViewModel : ViewModel() {
    lateinit var recommendations: LiveData<FavoriteMovieResponse>
    lateinit var tvRecommendations: LiveData<FavoriteTvResponse>

    fun getRecommendationsByMovieId(movieId: Long): LiveData<FavoriteMovieResponse> {
        recommendations = Api.getRecommendations(movieId)

        return recommendations
    }

    fun getRecommendationsByTvId(tvId: Long): LiveData<FavoriteTvResponse> {
        tvRecommendations = Api.getTvRecommendations(tvId)

        return tvRecommendations
    }
}