package com.decloudius.composetraining.ui.dashboard

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decloudius.composetraining.data.local.prefs.SettingsPreferences
import com.decloudius.composetraining.data.repository.PhotoRepository
import com.decloudius.composetraining.ui.theme.ThemeManager
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val photoRepository: PhotoRepository,
    private val themeManager: ThemeManager,
    private val settingsPreferences: SettingsPreferences
) : ViewModel() {

    private val _previewBitmap = MutableStateFlow<Bitmap?>(null)
    val previewBitmap: StateFlow<Bitmap?> = _previewBitmap.asStateFlow()

    private val _themeMode = MutableStateFlow(themeManager.getThemeMode())
    val themeMode: StateFlow<String> = _themeMode.asStateFlow()

    private val _languageCode = MutableStateFlow(settingsPreferences.getLanguage())
    val languageCode: StateFlow<String> = _languageCode.asStateFlow()

    private val _recreateNeeded = MutableSharedFlow<Boolean>(extraBufferCapacity = 1)
    val recreateNeeded = _recreateNeeded.asSharedFlow()

    fun onPhotoTaken(bitmap: Bitmap) {
        _previewBitmap.value = bitmap
    }

    fun savePhoto() {
        viewModelScope.launch {
            _previewBitmap.value?.let { bitmap ->
                photoRepository.savePhoto(bitmap)
                _previewBitmap.value = null
            }
        }
    }

    fun clearPreview() {
        _previewBitmap.value = null
    }

    fun setThemeMode(mode: String) {
        themeManager.setThemeMode(mode)
        _themeMode.value = mode
    }

    fun setLanguage(code: String) {
        settingsPreferences.setLanguage(code)
        _languageCode.value = code
        _recreateNeeded.tryEmit(true)
    }
}
