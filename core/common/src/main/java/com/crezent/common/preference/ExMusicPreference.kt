package com.crezent.common.preference

import android.content.SharedPreferences

class ExMusicPreference (
    private val sharedPreferences: SharedPreferences
) : Preference{
    override fun saveShouldShowOnboarding(showOnboarding: Boolean){
        sharedPreferences
            .edit()
            .putBoolean(Preference.KEY_SHOW_ONBOARD, showOnboarding)
            .apply()
    }

    override fun loggedInUsername(): String {
        TODO("Not yet implemented")
    }

    override fun saveLoggedInUsername() {
        TODO("Not yet implemented")
    }

    override fun apiKey(): String? {
        return sharedPreferences.getString(Preference.API_KEY,null)
    }

    override fun shouldShowOnboarding(): Boolean {
        return  sharedPreferences.getBoolean(Preference.KEY_SHOW_ONBOARD, true)
    }

    override fun saveApiKey(apiKey: String){
        sharedPreferences
            .edit()
            .putString(Preference.API_KEY, apiKey)
            .apply()
    }

    override fun alreadyLoggedIn(): Boolean {
        return  sharedPreferences.getBoolean(Preference.KEY_SHOW_ONBOARD, true)

    }
}