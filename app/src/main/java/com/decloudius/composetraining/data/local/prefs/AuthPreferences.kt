package com.decloudius.composetraining.data.local.prefs

import android.content.Context
import android.content.SharedPreferences

class AuthPreferences(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_PIN = "user_pin"
    }

    fun setPin(pin: String) {
        prefs.edit().putString(KEY_PIN, pin).apply()
    }

    fun getPin(): String? {
        return prefs.getString(KEY_PIN, null)
    }

    fun hasPin(): Boolean {
        return getPin() != null
    }

    fun clear() {
        prefs.edit().clear().apply()
    }
}
