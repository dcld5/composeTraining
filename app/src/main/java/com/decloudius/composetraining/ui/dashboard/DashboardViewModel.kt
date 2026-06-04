package com.decloudius.composetraining.ui.dashboard

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decloudius.composetraining.data.local.prefs.AuthPreferences
import com.decloudius.composetraining.data.local.prefs.SettingsPreferences
import com.decloudius.composetraining.data.repository.PhotoRepository
import com.decloudius.composetraining.ui.theme.ThemeManager
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * DashboardViewModel manages shared state for the DashboardActivity:
 * - Camera preview / save dialog
 * - Theme mode (light / dark / system)
 * - Language changes (triggers an Activity recreate event)
 * - PIN changes (triggers an Activity recreate event)
 */
class DashboardViewModel(
    private val photoRepository: PhotoRepository,
    private val themeManager: ThemeManager,
    private val settingsPreferences: SettingsPreferences,
    private val authPreferences: AuthPreferences
) : ViewModel() {

    /** Bitmap captured from the camera, waiting for user confirmation in the dialog. */
    private val _previewBitmap = MutableStateFlow<Bitmap?>(null)
    val previewBitmap: StateFlow<Bitmap?> = _previewBitmap.asStateFlow()

    /** Current theme mode string: "light", "dark", or "system". */
    private val _themeMode = MutableStateFlow(themeManager.getThemeMode())
    val themeMode: StateFlow<String> = _themeMode.asStateFlow()

    /** Current language code: "en" or "in". */
    private val _languageCode = MutableStateFlow(settingsPreferences.getLanguage())
    val languageCode: StateFlow<String> = _languageCode.asStateFlow()

    /** One-shot event for the Activity to call recreate() when language changes. */
    private val _recreateNeeded = MutableSharedFlow<Boolean>(extraBufferCapacity = 1)
    val recreateNeeded = _recreateNeeded.asSharedFlow()

    /** Called when the camera Intent returns a Bitmap. */
    fun onPhotoTaken(bitmap: Bitmap) {
        _previewBitmap.value = bitmap
    }

    /** User pressed Save in the preview dialog. */
    fun savePhoto() {
        viewModelScope.launch {
            _previewBitmap.value?.let { bitmap ->
                photoRepository.savePhoto(bitmap)
                _previewBitmap.value = null
            }
        }
    }

    /** User pressed Cancel in the preview dialog. */
    fun clearPreview() {
        _previewBitmap.value = null
    }

    /** Called from Settings when the user picks a new theme. */
    fun setThemeMode(mode: String) {
        themeManager.setThemeMode(mode)
        _themeMode.value = mode
    }

    /** Called from Settings when the user picks a new language. */
    fun setLanguage(code: String) {
        settingsPreferences.setLanguage(code)
        _languageCode.value = code
        _recreateNeeded.tryEmit(true)
    }

    /** Called from Settings when the user wants to change their PIN. */
    fun changePin(pin: Int) {
        authPreferences.setPin(pin.toString())
    }

    /** Called from Settings when the user wants to reset/clear their PIN. */
    fun resetPin() {
        authPreferences.clear()
    }
}
