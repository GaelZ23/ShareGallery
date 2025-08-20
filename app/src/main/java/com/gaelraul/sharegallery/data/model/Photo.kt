package com.gaelraul.sharegallery.data.model

import java.util.UUID

/**
 * Modelo de datos para una foto
 */
data class Photo(
    val id: String = "",
    val username: String = "",
    val imageBase64: String = "",
    val fileName: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val fileSize: Long = 0L
) {
    companion object {
        fun create(
            username: String,
            fileName: String,
            imageBase64: String
        ): Photo {
            return Photo(
                id = UUID.randomUUID().toString(),
                username = username,
                fileName = fileName,
                imageBase64 = imageBase64
            )
        }
    }
}
