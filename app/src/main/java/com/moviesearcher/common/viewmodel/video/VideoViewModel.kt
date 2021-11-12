package com.moviesearcher.common.viewmodel.video

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.moviesearcher.api.Api
import com.moviesearcher.common.model.videos.VideosResponse

class VideoViewModel : ViewModel() {
    private lateinit var video: LiveData<VideosResponse>
    private lateinit var tvVideo: LiveData<VideosResponse>

    fun getVideosByTvId(tvId: Long): LiveData<VideosResponse> {
        tvVideo = Api.tvVideos(tvId)

        return tvVideo
    }

    fun getVideosByMovieId(movieId: Long): LiveData<VideosResponse> {
        video = Api.videos(movieId)

        return video
    }
}