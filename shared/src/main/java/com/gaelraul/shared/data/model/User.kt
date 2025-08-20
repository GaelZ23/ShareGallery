package com.gaelraul.shared.data.model

import java.util.Date

/**
 * Modelo de datos que representa un usuario de la aplicación
 */
data class User(
    val username: String = "",
    val lastActive: Date = Date(),
    val totalPhotos: Int = 0
) {
    companion object {
        fun create(username: String): User {
            return User(
                username = username,
                lastActive = Date(),
                totalPhotos = 0
            )
        }
    }
}
