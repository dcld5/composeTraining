package com.decloudius.composetraining.data.repository

import com.decloudius.composetraining.data.local.db.ProfileDao
import com.decloudius.composetraining.data.local.db.entity.ProfileEntity
import com.decloudius.composetraining.domain.model.Profile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Repository for the single user profile.
 */
class ProfileRepository(private val profileDao: ProfileDao) {

    suspend fun saveProfile(profile: Profile) {
        val entity = ProfileEntity(
            id = profile.id,
            name = profile.name,
            imagePath = profile.imagePath
        )
        profileDao.saveProfile(entity)
    }

    fun getProfile(): Flow<Profile?> {
        return profileDao.getProfile().map { entity ->
            entity?.let {
                Profile(
                    id = it.id,
                    name = it.name,
                    imagePath = it.imagePath
                )
            }
        }
    }
}
