package com.gaelraul.sharegallery.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gaelraul.sharegallery.data.model.Photo
import com.gaelraul.sharegallery.data.repository.PhotoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

/**
 * ViewModel principal que maneja la lógica de negocio de ShareGallery
 */
class ShareGalleryViewModel(
    private val photoRepository: PhotoRepository = PhotoRepository()
) : ViewModel() {
    
    // Estado de la aplicación
    private val _uiState = MutableStateFlow(ShareGalleryUiState())
    val uiState: StateFlow<ShareGalleryUiState> = _uiState.asStateFlow()
    
    // Estado de las fotos
    private val _photos = MutableStateFlow<List<Photo>>(emptyList())
    val photos: StateFlow<List<Photo>> = _photos.asStateFlow()
    
    // Estado de carga
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    // Estado de error
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    /**
     * Inicia el stream de fotos en tiempo real
     */
    fun startPhotosStream() {
        viewModelScope.launch {
            photoRepository.getPhotosStream()
                .catch { e ->
                    _error.value = "Error al cargar fotos: ${e.message}"
                }
                .collect { photos ->
                    _photos.value = photos
                    _isLoading.value = false
                }
        }
    }
    
    /**
     * Sube una nueva foto
     */
    fun uploadPhoto(username: String, imageBase64: String, fileName: String) {
        if (username.isBlank()) {
            _error.value = "El nombre de usuario no puede estar vacío"
            return
        }
        
        _isLoading.value = true
        _error.value = null
        
        viewModelScope.launch {
            try {
                // Subir a Firebase usando el repositorio
                val result = photoRepository.uploadPhoto(username, imageBase64, fileName)
                
                result.fold(
                    onSuccess = { photo ->
                        _uiState.value = _uiState.value.copy(
                            currentUsername = username,
                            lastUploadedPhoto = photo
                        )
                        _isLoading.value = false
                    },
                    onFailure = { exception ->
                        _error.value = "Error al subir foto: ${exception.message}"
                        _isLoading.value = false
                    }
                )
            } catch (e: Exception) {
                _error.value = "Error al procesar imagen: ${e.message}"
                _isLoading.value = false
            }
        }
    }
    
    /**
     * Establece el nombre de usuario actual
     */
    fun setCurrentUsername(username: String) {
        _uiState.value = _uiState.value.copy(currentUsername = username)
    }
    
    /**
     * Limpia el error
     */
    fun clearError() {
        _error.value = null
    }
    
    /**
     * Elimina una foto
     */
    fun deletePhoto(photoId: String) {
        _photos.value = _photos.value.filter { it.id != photoId }
    }
}

/**
 * Estado de la UI de ShareGallery
 */
data class ShareGalleryUiState(
    val currentUsername: String = "",
    val lastUploadedPhoto: Photo? = null
)
