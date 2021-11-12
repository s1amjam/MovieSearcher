package com.moviesearcher.common.viewmodel.images

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.moviesearcher.api.Api
import com.moviesearcher.common.model.images.ImagesResponse

class ImagesViewModel : ViewModel() {
    private lateinit var movieImages: LiveData<ImagesResponse>
    private lateinit var tvImages: LiveData<ImagesResponse>

    fun getImagesByMovieId(movieId: Long): LiveData<ImagesResponse> {
        movieImages = Api.images(movieId)

        return movieImages
    }

    fun getImagesByTvId(tvId: Long): LiveData<ImagesResponse> {
        tvImages = Api.tvImages(tvId)

        return tvImages
    }
}