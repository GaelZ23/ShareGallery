package com.gaelraul.shared.data.repository

import com.gaelraul.shared.data.model.Photo
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.UUID

/**
 * Repositorio para manejar las operaciones de fotos con Firebase
 */
class PhotoRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()
) {
    
    companion object {
        private const val PHOTOS_COLLECTION = "photos"
        private const val STORAGE_PHOTOS_PATH = "photos"
    }
    
    /**
     * Sube una foto a Firebase Storage y guarda la referencia en Firestore
     */
    suspend fun uploadPhoto(
        username: String,
        imageBytes: ByteArray,
        fileName: String
    ): Result<Photo> {
        return try {
            // Generar ID único para la foto
            val photoId = UUID.randomUUID().toString()
            
            // Referencia en Storage
            val storageRef = storage.reference
                .child(STORAGE_PHOTOS_PATH)
                .child(photoId)
                .child(fileName)
            
            // Subir imagen a Storage
            val uploadTask = storageRef.putBytes(imageBytes).await()
            val downloadUrl = storageRef.downloadUrl.await()
            
            // Crear objeto Photo
            val photo = Photo.create(
                username = username,
                imageUrl = downloadUrl.toString(),
                fileName = fileName,
                fileSize = imageBytes.size.toLong()
            )
            
            // Guardar en Firestore
            firestore.collection(PHOTOS_COLLECTION)
                .document(photoId)
                .set(photo.copy(id = photoId))
                .await()
            
            Result.success(photo.copy(id = photoId))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Obtiene todas las fotos en tiempo real
     */
    fun getPhotosStream(): Flow<List<Photo>> = flow {
        val snapshot = firestore.collection(PHOTOS_COLLECTION)
            .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .get()
            .await()
        
        val photos = snapshot.documents.mapNotNull { doc ->
            doc.toObject(Photo::class.java)?.copy(id = doc.id)
        }
        
        emit(photos)
    }
    
    /**
     * Obtiene las fotos de un usuario específico
     */
    suspend fun getPhotosByUsername(username: String): List<Photo> {
        val snapshot = firestore.collection(PHOTOS_COLLECTION)
            .whereEqualTo("username", username)
            .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .get()
            .await()
        
        return snapshot.documents.mapNotNull { doc ->
            doc.toObject(Photo::class.java)?.copy(id = doc.id)
        }
    }
    
    /**
     * Elimina una foto
     */
    suspend fun deletePhoto(photoId: String): Result<Unit> {
        return try {
            // Obtener la foto para obtener la URL de Storage
            val photoDoc = firestore.collection(PHOTOS_COLLECTION)
                .document(photoId)
                .get()
                .await()
            
            val photo = photoDoc.toObject(Photo::class.java)
            
            // Eliminar de Firestore
            firestore.collection(PHOTOS_COLLECTION)
                .document(photoId)
                .delete()
                .await()
            
            // Eliminar de Storage si existe
            photo?.let {
                try {
                    val storageRef = storage.getReferenceFromUrl(it.imageUrl)
                    storageRef.delete().await()
                } catch (e: Exception) {
                    // Si falla la eliminación de Storage, no es crítico
                    println("Error deleting from Storage: ${e.message}")
                }
            }
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
