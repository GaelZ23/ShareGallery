package com.gaelraul.sharegallery.utils

import android.content.Context
import android.net.Uri
import android.provider.MediaStore

/**
 * Utilidades para manejar imágenes
 */
object ImagePicker {
    
    /**
     * Obtiene el nombre del archivo de la URI
     */
    fun getFileName(context: Context, uri: Uri): String {
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        return cursor?.use {
            val nameIndex = it.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME)
            it.moveToFirst()
            it.getString(nameIndex) ?: "image_${System.currentTimeMillis()}.jpg"
        } ?: "image_${System.currentTimeMillis()}.jpg"
    }
    
    /**
     * Obtiene el tamaño del archivo en bytes
     */
    fun getFileSize(context: Context, uri: Uri): Long {
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        return cursor?.use {
            val sizeIndex = it.getColumnIndex(MediaStore.MediaColumns.SIZE)
            it.moveToFirst()
            it.getLong(sizeIndex)
        } ?: 0L
    }
    
    /**
     * Convierte URI a bytes para subir a Firebase
     */
    fun uriToBytes(context: Context, uri: Uri): ByteArray? {
        return try {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                inputStream.readBytes()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
