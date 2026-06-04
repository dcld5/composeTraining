package com.decloudius.composetraining.data.local.prefs

import android.content.Context
import android.content.SharedPreferences

/**
 * AuthPreferences stores login-related data in SharedPreferences.
 * SharedPreferences is Android's simple key-value storage perfect for small data
 * like a PIN code, flags, or tokens. Think of it as a tiny settings file.
 */
class AuthPreferences(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_PIN = "user_pin"
        private const val KEY_BIOMETRIC = "biometric_enabled"
    }

    /** Save the 6-digit PIN as a plain string (in a real app you might hash it). */
    fun setPin(pin: String) {
        prefs.edit().putString(KEY_PIN, pin).apply()
    }

    /** Retrieve the saved PIN, or null if the user never set one. */
    fun getPin(): String? {
        return prefs.getString(KEY_PIN, null)
    }

    /** Check if the user has already created a PIN (used on first launch). */
    fun hasPin(): Boolean {
        return getPin() != null
    }

    /** Enable or disable biometric login option. */
    fun setBiometricEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_BIOMETRIC, enabled).apply()
    }

    /** Check whether biometric login is turned on. */
    fun isBiometricEnabled(): Boolean {
        return prefs.getBoolean(KEY_BIOMETRIC, false)
    }

    /** Clear everything (useful for logout/reset). */
    fun clear() {
        prefs.edit().clear().apply()
    }
}
