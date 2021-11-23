package com.moviesearcher.person.viewmodel.personimages

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.moviesearcher.person.model.images.PersonImagesResponse
import com.moviesearcher.api.Api

class PersonImagesViewModel : ViewModel() {
    lateinit var personImages: LiveData<PersonImagesResponse>

    fun getImagesByPersonId(personId: Long): LiveData<PersonImagesResponse> {
        personImages = Api.personImages(personId)

        return personImages
    }
}