package com.decloudius.composetraining.data.repository

import android.content.Context
import android.graphics.Bitmap
import com.decloudius.composetraining.data.local.db.PhotoDao
import com.decloudius.composetraining.data.local.db.entity.PhotoEntity
import com.decloudius.composetraining.domain.model.Photo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.File
import java.io.FileOutputStream

/**
 * PhotoRepository handles saving images to local storage AND recording them in Room.
 * The UI never deals with file paths or bitmaps directly; it just asks:
 * "take this bitmap" or "give me the list of photos".
 */
class PhotoRepository(
    private val context: Context,
    private val photoDao: PhotoDao
) {

    /**
     * Save a Bitmap as a PNG file in the app's private files directory,
     * then insert the path into the Room database.
     * Returns the generated database ID.
     */

    // suspend modifier makes this function run on a background thread.
    suspend fun savePhoto(bitmap: Bitmap): Long {
        // Create a unique filename based on the current time.
        val filename = "photo_${System.currentTimeMillis()}.png"
        val file = File(context.filesDir, filename)

        // Compress the bitmap and write it to the file.
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }

        // Insert only the path into the database (not the whole image).
        val entity = PhotoEntity(filePath = file.absolutePath)
        return photoDao.insertPhoto(entity)
    }

    // can use this to save a photo with a custom filename
    suspend fun savePhoto(bitmap: Bitmap, fileName: String): Long {
        val file = File(context.filesDir, fileName)
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }
        val entity = PhotoEntity(filePath = file.absolutePath)
        return photoDao.insertPhoto(entity)
    }

    /**
     * Expose a reactive Flow of all photos.
     * We map Room entities to domain models so the UI stays clean.
     */
    fun getAllPhotos(): Flow<List<Photo>> {
        return photoDao.getAllPhotos().map { list ->
            list.map { entity ->
                Photo(
                    id = entity.id,
                    filePath = entity.filePath,
                    timestamp = entity.timestamp
                )
            }
        }
    }

    /** Delete a photo from the database (and optionally the file). */
    suspend fun deletePhoto(photo: Photo) {
        photoDao.deleteById(photo.id)
        // Also delete the file to avoid filling storage.
        File(photo.filePath).delete()
    }
}
