package com.moviesearcher.movie.viewmodel.recommendations

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.moviesearcher.api.Api
import com.moviesearcher.favorite.movie.model.FavoriteMovieResponse

class RecommendationsViewModel : ViewModel() {
    lateinit var recommendations: LiveData<FavoriteMovieResponse>

    fun getRecommendationsByMovieId(movieId: Long): LiveData<FavoriteMovieResponse> {
        recommendations = Api.getRecommendations(movieId)

        return recommendations
    }
}