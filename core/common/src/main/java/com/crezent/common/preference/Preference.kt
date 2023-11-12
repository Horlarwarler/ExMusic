package com.crezent.common.preference

interface Preference {
    fun saveApiKey(apiKey:String)

    fun saveShouldShowOnboarding(showOnboarding:Boolean)
    fun shouldShowOnboarding() :Boolean

    fun alreadyLoggedIn(): Boolean

    fun loggedInUsername():String

    fun saveLoggedInUsername()

    fun apiKey():String?

    companion object {
        const val KEY_SHOW_ONBOARD = "show_onboard"
        const val API_KEY = "save_api_key"
    }

}