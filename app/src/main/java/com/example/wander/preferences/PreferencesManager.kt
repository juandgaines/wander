package com.example.wander.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

class PreferencesManager(context: Context) {

    private var sharedPreferenceInstance: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)

    var token: String?
        get() = sharedPreferenceInstance.getString(PrefConsts.TOKEN, "")
        set(value) = sharedPreferenceInstance.edit().putString(PrefConsts.TOKEN, value).apply()


    companion object {
        @Volatile
        private var INSTANCE: PreferencesManager? = null

        fun getPreferenceProvider(context: Context): PreferencesManager {

            return INSTANCE ?: synchronized(this) {
                PreferencesManager(context)
            }
        }

    }

}