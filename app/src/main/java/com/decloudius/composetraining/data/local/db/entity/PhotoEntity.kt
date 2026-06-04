package com.decloudius.composetraining.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity = a table in the SQLite database created by Room.
 * This class describes what columns the "photos" table has.
 * Room automatically creates the table from this class.
 *
 * @Entity(tableName = "photos") tells Room to name the table "photos".
 * @PrimaryKey(autoGenerate = true) means each row gets a unique ID automatically.
 */
@Entity(tableName = "photos")
data class PhotoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    /** File path where the image is stored inside the app's private storage. */
    val filePath: String,

    /** Timestamp in milliseconds when the photo was taken. */
    val timestamp: Long = System.currentTimeMillis()
)
