package com.decloudius.composetraining.ui

import android.content.Context
import androidx.fragment.app.FragmentActivity
import com.decloudius.composetraining.util.LocaleHelper

abstract class BaseLocaleActivity : FragmentActivity() {

    override fun attachBaseContext(newBase: Context) {

        val prefs = newBase.getSharedPreferences("settings_prefs", Context.MODE_PRIVATE)
        val lang = prefs.getString("language_code", "en") ?: "en"
        super.attachBaseContext(LocaleHelper.setLocale(newBase, lang))
    }
}
