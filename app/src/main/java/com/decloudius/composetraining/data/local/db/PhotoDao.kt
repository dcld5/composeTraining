package com.decloudius.composetraining.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.decloudius.composetraining.data.local.db.entity.PhotoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotoDao {

    @Insert
    suspend fun insertPhoto(photo: PhotoEntity): Long

    @Query("SELECT * FROM photos ORDER BY timestamp DESC")
    fun getAllPhotos(): Flow<List<PhotoEntity>>

    @Query("DELETE FROM photos WHERE id = :id")
    suspend fun deleteById(id: Long)
}
