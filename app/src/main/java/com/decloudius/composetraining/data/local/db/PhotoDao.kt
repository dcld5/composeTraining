package com.decloudius.composetraining.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.decloudius.composetraining.data.local.db.entity.PhotoEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO (Data Access Object) is the bridge between your Kotlin code and the database.
 * You define SQL-like methods here using Room annotations, and Room generates the actual SQL.
 * Using Flow means the UI automatically updates whenever the database changes (reactive!).
 */
@Dao
interface PhotoDao {

    /** Insert a new photo row. Room handles the SQL INSERT statement. */
    @Insert
    suspend fun insertPhoto(photo: PhotoEntity): Long

    /** Get all photos ordered by newest first. Flow emits again on every change. */
    @Query("SELECT * FROM photos ORDER BY timestamp DESC")
    fun getAllPhotos(): Flow<List<PhotoEntity>>

    /** Delete a specific photo by its ID. */
    @Query("DELETE FROM photos WHERE id = :id")
    suspend fun deleteById(id: Long)
}
