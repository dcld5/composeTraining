package com.decloudius.composetraining.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decloudius.composetraining.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * LoginViewModel is the BRAIN of the login screen.
 * In MVVM, the ViewModel holds the screen's state and logic.
 * It survives rotation (screen turning sideways) so the user doesn't lose their PIN input.
 *
 * Key rule: NEVER put Android UI classes (Context, Toast, etc.) inside a ViewModel.
 * The ViewModel only exposes plain Kotlin data (Strings, Booleans, Ints).
 * The Composable (UI) layer observes these states and draws them.
 */
class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    /** The PIN the user is currently typing. */
    private val _pin = MutableStateFlow("")
    val pin: StateFlow<String> = _pin.asStateFlow()

    /** Error message shown when PIN is wrong or invalid. null = no error. */
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    /** true if a PIN was saved before (returning user), false if first-time setup. */
    private val _isPinSet = MutableStateFlow(false)
    val isPinSet: StateFlow<Boolean> = _isPinSet.asStateFlow()

    /** true if the user turned on biometric login in the past. */
    private val _isBiometricEnabled = MutableStateFlow(false)
    val isBiometricEnabled: StateFlow<Boolean> = _isBiometricEnabled.asStateFlow()

    /** One-shot event: when true, the Activity should open DashboardActivity. */
    private val _navigateToDashboard = MutableStateFlow(false)
    val navigateToDashboard: StateFlow<Boolean> = _navigateToDashboard.asStateFlow()

    init {
        // Check local storage as soon as the ViewModel is created.
        _isPinSet.value = authRepository.hasPin()
        _isBiometricEnabled.value = authRepository.isBiometricEnabled()
    }

    /** Called every time the user types or deletes a digit. */
    fun onPinChange(newPin: String) {
        // Only accept digits and max 6 characters.
        if (newPin.length <= 6 && newPin.all { it.isDigit() }) {
            _pin.value = newPin
            _error.value = null
        }
    }

    /** User tapped the Login button (existing PIN flow). */
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

    /** User tapped Save PIN button (first-time setup flow). */
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

    /** Called by the Activity when the biometric sensor succeeds. */
    fun onBiometricSuccess() {
        _navigateToDashboard.value = true
    }

    /** Reset the navigation event so it doesn't fire twice. */
    fun onNavigationHandled() {
        _navigateToDashboard.value = false
    }

    /** Clear the error after the user has seen it. */
    fun onErrorShown() {
        _error.value = null
    }
}
