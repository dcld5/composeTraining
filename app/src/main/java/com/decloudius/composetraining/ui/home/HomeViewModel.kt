package com.decloudius.composetraining.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decloudius.composetraining.data.repository.PhotoRepository
import com.decloudius.composetraining.domain.model.Photo
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(private val photoRepository: PhotoRepository) : ViewModel() {

    val photos: StateFlow<List<Photo>> = photoRepository.getAllPhotos()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun deletePhoto(photo: Photo) {
        viewModelScope.launch {
            photoRepository.deletePhoto(photo)
        }
    }
}
