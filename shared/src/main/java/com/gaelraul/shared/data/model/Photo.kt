package com.gaelraul.shared.data.model

import java.util.Date

/**
 * Modelo de datos que representa una foto en la galería compartida
 */
data class Photo(
    val id: String = "",
    val username: String = "",
    val imageUrl: String = "",
    val fileName: String = "",
    val timestamp: Date = Date(),
    val fileSize: Long = 0L
) {
    companion object {
        fun create(
            username: String,
            imageUrl: String,
            fileName: String,
            fileSize: Long
        ): Photo {
            return Photo(
                username = username,
                imageUrl = imageUrl,
                fileName = fileName,
                fileSize = fileSize,
                timestamp = Date()
            )
        }
    }
}
