package com.decloudius.composetraining.ui.login

import androidx.lifecycle.ViewModel
import com.decloudius.composetraining.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _pin = MutableStateFlow("")
    val pin: StateFlow<String> = _pin.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _isPinSet = MutableStateFlow(false)
    val isPinSet: StateFlow<Boolean> = _isPinSet.asStateFlow()

    private val _navigateToDashboard = MutableStateFlow(false)
    val navigateToDashboard: StateFlow<Boolean> = _navigateToDashboard.asStateFlow()

    init {

        _isPinSet.value = authRepository.hasPin()
    }

    fun onPinChange(newPin: String) {

        if (newPin.length <= 6 && newPin.all { it.isDigit() }) {
            _pin.value = newPin
            _error.value = null
        }
    }

    fun onLoginClick() {
        val current = _pin.value
        if (current.length != 6) {
            _error.value = "PIN must be 6 digits"
            return
        }
        if (authRepository.validatePin(current)) {
            _navigateToDashboard.value = true
        } else {
            _error.value = "Wrong PIN"
        }
    }

    fun onCreatePinClick() {
        val current = _pin.value
        if (current.length != 6) {
            _error.value = "PIN must be 6 digits"
            return
        }
        authRepository.setPin(current)
        _isPinSet.value = true
        _pin.value = ""
    }

    fun onNavigationHandled() {
        _navigateToDashboard.value = false
    }

    fun onErrorShown() {
        _error.value = null
    }
}
