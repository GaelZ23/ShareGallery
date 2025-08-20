package com.gaelraul.sharegallery.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

/**
 * Gestor de permisos para la aplicación ShareGallery
 */
object PermissionManager {
    
    /**
     * Verifica si tenemos permisos para acceder a la galería
     */
    fun hasGalleryPermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13+ usa READ_MEDIA_IMAGES
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            // Android 12 y anteriores usan READ_EXTERNAL_STORAGE
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        }
    }
    
    /**
     * Obtiene el permiso que necesitamos solicitar
     */
    fun getRequiredPermission(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
    }
    
    /**
     * Verifica si debemos mostrar explicación del permiso
     */
    fun shouldShowPermissionRationale(): Boolean {
        // Por ahora retornamos false, pero se puede implementar lógica más compleja
        return false
    }
}
