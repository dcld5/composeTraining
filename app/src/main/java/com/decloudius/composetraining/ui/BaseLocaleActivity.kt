package com.decloudius.composetraining.ui

import android.content.Context
import androidx.fragment.app.FragmentActivity
import com.decloudius.composetraining.util.LocaleHelper

/**
 * A base Activity that applies the saved language before the Activity is even created.
 * By overriding attachBaseContext, every text loaded from strings.xml will use
 * the language the user selected in Settings.
 *
 * We extend FragmentActivity (not AppCompatActivity) because:
 * 1. BiometricPrompt requires a FragmentActivity.
 * 2. FragmentActivity extends ComponentActivity, which supports Compose's setContent { }.
 * 3. We don't need the AppCompat theme overhead.
 */
abstract class BaseLocaleActivity : FragmentActivity() {

    override fun attachBaseContext(newBase: Context) {
        // Read the language code directly from SharedPreferences
        // (we can't use Koin injection here because attachBaseContext runs very early).
        val prefs = newBase.getSharedPreferences("settings_prefs", Context.MODE_PRIVATE)
        val lang = prefs.getString("language_code", "en") ?: "en"
        super.attachBaseContext(LocaleHelper.setLocale(newBase, lang))
    }
}
