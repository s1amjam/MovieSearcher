package com.moviesearcher.movie.viewmodel.images

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.moviesearcher.api.Api
import com.moviesearcher.common.model.images.ImagesResponse

class ImagesViewModel : ViewModel() {
    private lateinit var images: LiveData<ImagesResponse>

    fun getImagesByMovieId(movieId: Long): LiveData<ImagesResponse> {
        images = Api.images(movieId)

        return images
    }
}