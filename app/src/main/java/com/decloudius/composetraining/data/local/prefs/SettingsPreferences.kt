package com.decloudius.composetraining.data.local.prefs

import android.content.Context
import android.content.SharedPreferences

/**
 * SettingsPreferences stores app-wide settings like language and theme.
 * Keeping it separate from AuthPreferences is Clean Architecture: each class has one job.
 */
class SettingsPreferences(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("settings_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_LANGUAGE = "language_code"
        private const val DEFAULT_LANGUAGE = "en" // English by default
    }

    /** Save language code: "en" for English, "in" for Indonesian. */
    fun setLanguage(code: String) {
        prefs.edit().putString(KEY_LANGUAGE, code).apply()
    }

    /** Read the saved language code. */
    fun getLanguage(): String {
        return prefs.getString(KEY_LANGUAGE, DEFAULT_LANGUAGE) ?: DEFAULT_LANGUAGE
    }
}
