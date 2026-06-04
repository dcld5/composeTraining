package com.decloudius.composetraining.data.repository

import com.decloudius.composetraining.data.local.prefs.AuthPreferences

class AuthRepository(private val prefs: AuthPreferences) {

    fun setPin(pin: String) = prefs.setPin(pin)
    fun getPin(): String? = prefs.getPin()
    fun hasPin(): Boolean = prefs.hasPin()
    fun validatePin(input: String): Boolean = prefs.getPin() == input
    fun clear() = prefs.clear()
}
