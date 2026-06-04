package com.decloudius.composetraining.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.decloudius.composetraining.data.local.db.entity.ProfileEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO for the single-row profile table.
 * We use OnConflictStrategy.REPLACE so inserting with ID=1 always overwrites the old row.
 */
@Dao
interface ProfileDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProfile(profile: ProfileEntity)

    @Query("SELECT * FROM profile WHERE id = 1")
    fun getProfile(): Flow<ProfileEntity?>
}
