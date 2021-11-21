package com.moviesearcher.actor.viewmodel.personCombinedCredits

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.moviesearcher.actor.model.combinedcredits.CombinedCreditsResponse
import com.moviesearcher.api.Api

class PersonCombinedCreditsViewModel : ViewModel() {
    lateinit var personCombinedCredits: LiveData<CombinedCreditsResponse>

    fun getCombinedCreditsByPersonId(personId: Long): LiveData<CombinedCreditsResponse> {
        personCombinedCredits= Api.personCombinedCredits(personId)

        return personCombinedCredits
    }
}