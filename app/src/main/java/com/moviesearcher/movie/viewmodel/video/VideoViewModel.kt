package com.moviesearcher.movie.viewmodel.video

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.moviesearcher.api.Api
import com.moviesearcher.common.model.videos.VideosResponse

class VideoViewModel : ViewModel() {
    private lateinit var video: LiveData<VideosResponse>

    fun getVideosByMovieId(movieId: Long): LiveData<VideosResponse> {
        video = Api.videos(movieId)

        return video
    }
}