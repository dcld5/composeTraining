package com.decloudius.composetraining.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decloudius.composetraining.data.repository.ProfileRepository
import com.decloudius.composetraining.domain.model.Profile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ProfileViewModel manages the single user profile row.
 * It reads the existing profile from Room and exposes name + imagePath as Compose states.
 */
class ProfileViewModel(private val profileRepository: ProfileRepository) : ViewModel() {

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name.asStateFlow()

    private val _imagePath = MutableStateFlow<String?>(null)
    val imagePath: StateFlow<String?> = _imagePath.asStateFlow()

    private val _saved = MutableStateFlow(false)
    val saved: StateFlow<Boolean> = _saved.asStateFlow()

    init {
        // When the ViewModel starts, collect the existing profile from Room.
        viewModelScope.launch {
            profileRepository.getProfile().collect { profile ->
                _name.value = profile?.name ?: ""
                _imagePath.value = profile?.imagePath
            }
        }
    }

    fun onNameChange(newName: String) {
        _name.value = newName
    }

    fun onImagePicked(path: String?) {
        _imagePath.value = path
    }

    fun saveProfile() {
        viewModelScope.launch {
            profileRepository.saveProfile(
                Profile(name = _name.value, imagePath = _imagePath.value)
            )
            _saved.value = true
        }
    }

    fun onSaveHandled() {
        _saved.value = false
    }
}
