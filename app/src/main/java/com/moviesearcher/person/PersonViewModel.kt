package com.moviesearcher.person

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moviesearcher.api.ApiService
import com.moviesearcher.common.utils.Constants.PERSON_ID
import com.moviesearcher.common.utils.Resource
import com.moviesearcher.person.model.combinedcredits.CombinedCreditsResponse
import com.moviesearcher.person.model.images.PersonImagesResponse
import com.moviesearcher.person.model.person.PersonResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonViewModel @Inject constructor(private val savedStateHandle: SavedStateHandle) :
    ViewModel() {
    private val person = MutableLiveData<Resource<PersonResponse>>()
    private val personCombinedCredits = MutableLiveData<Resource<CombinedCreditsResponse>>()
    private val personImages = MutableLiveData<Resource<PersonImagesResponse>>()

    init {
        fetchPerson()
        fetchPersonCombinedCredits()
        fetchPersonImages()
    }

    private fun fetchPerson() {
        viewModelScope.launch {
            person.postValue(Resource.loading(null))
            try {
                val personFromApi =
                    savedStateHandle.get<Long>(PERSON_ID)?.let { ApiService.create().person(it) }
                person.postValue(Resource.success(personFromApi))
            } catch (e: Exception) {
                person.postValue(Resource.error(e.toString(), null))
            }
        }
    }

    fun getPerson(): MutableLiveData<Resource<PersonResponse>> {
        return person
    }

    private fun fetchPersonCombinedCredits() {
        viewModelScope.launch {
            personCombinedCredits.postValue(Resource.loading(null))
            try {
                val personCombinedCreditsFromApi =
                    savedStateHandle.get<Long>(PERSON_ID)
                        ?.let { ApiService.create().personCombinedCredits(it) }
                personCombinedCredits.postValue(Resource.success(personCombinedCreditsFromApi))
            } catch (e: Exception) {
                personCombinedCredits.postValue(Resource.error(e.toString(), null))
            }
        }
    }

    fun getPersonCombinedCredits(): MutableLiveData<Resource<CombinedCreditsResponse>> {
        return personCombinedCredits
    }

    private fun fetchPersonImages() {
        viewModelScope.launch {
            personImages.postValue(Resource.loading(null))
            try {
                val personImagesFromApi =
                    savedStateHandle.get<Long>(PERSON_ID)
                        ?.let { ApiService.create().personImages(it) }
                personImages.postValue(Resource.success(personImagesFromApi))
            } catch (e: Exception) {
                personImages.postValue(Resource.error(e.toString(), null))
            }
        }
    }

    fun getPersonImages(): MutableLiveData<Resource<PersonImagesResponse>> {
        return personImages
    }
}