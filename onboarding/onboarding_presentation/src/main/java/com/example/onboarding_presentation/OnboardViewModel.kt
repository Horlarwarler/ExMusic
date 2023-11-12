package com.example.onboarding_presentation

import androidx.lifecycle.ViewModel
import com.crezent.common.preference.Preference
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class OnboardViewModel @Inject constructor(
    private val preference: Preference
) : ViewModel(){

    fun saveShowOnboarding(){
        preference.saveShouldShowOnboarding(false)
    }
}