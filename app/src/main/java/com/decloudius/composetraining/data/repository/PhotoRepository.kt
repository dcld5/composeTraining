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

class PhotoRepository(
    private val context: Context,
    private val photoDao: PhotoDao
) {

    suspend fun savePhoto(bitmap: Bitmap): Long {

        val filename = "photo_${System.currentTimeMillis()}.png"
        val file = File(context.filesDir, filename)

        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }

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

    suspend fun deletePhoto(photo: Photo) {
        photoDao.deleteById(photo.id)

        File(photo.filePath).delete()
    }
}
