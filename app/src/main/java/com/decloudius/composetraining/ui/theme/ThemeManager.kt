package com.decloudius.composetraining.ui.theme

import android.content.Context
import android.content.SharedPreferences

/**
 * ThemeManager is a small helper class that remembers the user's theme choice
 * using SharedPreferences (Android's built-in key-value local storage).
 *
 * Theme modes:
 * - "system" -> follow the phone's dark/light setting
 * - "light"  -> always light
 * - "dark"   -> always dark
 */
class ThemeManager(context: Context) {

    // SharedPreferences is like a tiny JSON file on the phone.
    // We give it a name ("theme_prefs") so it doesn't mix with other data.
    private val prefs: SharedPreferences =
        context.getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_THEME = "theme_mode"
        private const val DEFAULT_THEME = "system"
    }

    /** Save the chosen theme mode as a simple string. */
    fun setThemeMode(mode: String) {
        prefs.edit().putString(KEY_THEME, mode).apply()
    }

    /** Read back the saved theme mode (defaults to "system" if never set). */
    fun getThemeMode(): String {
        return prefs.getString(KEY_THEME, DEFAULT_THEME) ?: DEFAULT_THEME
    }
}
