package com.moviesearcher.actor.viewmodel.person

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.moviesearcher.actor.model.person.PersonResponse
import com.moviesearcher.api.Api

class PersonViewModel : ViewModel() {
    lateinit var person: LiveData<PersonResponse>

    fun getPersonById(personId: Long): LiveData<PersonResponse> {
        person = Api.person(personId)

        return person
    }
}