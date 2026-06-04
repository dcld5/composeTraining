package com.decloudius.composetraining.data.repository

import com.decloudius.composetraining.data.local.prefs.AuthPreferences

/**
 * Repository is a Clean Architecture pattern that hides WHERE the data comes from.
 * The UI (ViewModel) asks the Repository for data, and the Repository decides
 * whether to fetch from SharedPreferences, a server, or a database.
 *
 * Right now this one is very simple because auth data only lives in SharedPreferences.
 */
class AuthRepository(private val prefs: AuthPreferences) {

    fun setPin(pin: String) = prefs.setPin(pin)
    fun getPin(): String? = prefs.getPin()
    fun hasPin(): Boolean = prefs.hasPin()
    fun validatePin(input: String): Boolean = prefs.getPin() == input
    fun clear() = prefs.clear()
}
