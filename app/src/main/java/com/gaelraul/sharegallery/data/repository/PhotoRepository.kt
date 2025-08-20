package com.gaelraul.sharegallery.data.repository

import com.gaelraul.sharegallery.data.model.Photo
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

/**
 * Repositorio simple para manejar fotos usando Firebase Realtime Database
 */
class PhotoRepository {
    
    private val database: FirebaseDatabase = Firebase.database("https://sharegallery-fbf48-default-rtdb.firebaseio.com")
    private val photosRef = database.getReference("photos")
    
    /**
     * Sube una nueva foto
     */
    suspend fun uploadPhoto(username: String, imageBase64: String, fileName: String): Result<Photo> {
        return try {
            val photo = Photo.create(username, fileName, imageBase64)
            
            // Guardar en Realtime Database
            photosRef.child(photo.id).setValue(photo).await()
            
            Result.success(photo)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Obtiene stream de fotos en tiempo real
     */
    fun getPhotosStream(): Flow<List<Photo>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val photos = mutableListOf<Photo>()
                
                for (childSnapshot in snapshot.children) {
                    val photo = childSnapshot.getValue<Photo>()
                    photo?.let { photos.add(it) }
                }
                
                // Ordenar por timestamp (más recientes primero)
                photos.sortByDescending { it.timestamp }
                
                trySend(photos)
            }
            
            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        
        photosRef.addValueEventListener(listener)
        
        awaitClose {
            photosRef.removeEventListener(listener)
        }
    }
    
    /**
     * Elimina una foto
     */
    suspend fun deletePhoto(photoId: String): Result<Unit> {
        return try {
            photosRef.child(photoId).removeValue().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
