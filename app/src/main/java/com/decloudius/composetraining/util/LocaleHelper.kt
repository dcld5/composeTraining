package com.decloudius.composetraining.util

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import java.util.Locale

/**
 * LocaleHelper is a utility object that helps us change the app language at runtime.
 * Normally Android uses the phone language. With this helper we can force a specific language
 * (English or Indonesian) for our app only, without changing the phone settings.
 */
object LocaleHelper {

    /**
     * Wraps an existing Context with a new Locale configuration.
     * We call this from attachBaseContext() in our Activity so the entire Activity
     * thinks the user's language is the one we selected.
     */
    fun setLocale(context: Context, languageCode: String): Context {
        return updateResources(context, languageCode)
    }

    private fun updateResources(context: Context, languageCode: String): Context {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(locale)
            context.createConfigurationContext(config)
        } else {
            @Suppress("DEPRECATION")
            config.locale = locale
            @Suppress("DEPRECATION")
            context.resources.updateConfiguration(config, context.resources.displayMetrics)
            context
        }
    }

    fun getLocale(resources: Resources): Locale {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            resources.configuration.locales[0]
        } else {
            @Suppress("DEPRECATION")
            resources.configuration.locale
        }
    }
}
