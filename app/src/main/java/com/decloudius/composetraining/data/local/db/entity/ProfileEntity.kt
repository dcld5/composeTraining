package com.decloudius.composetraining.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity = a table in the database.
 * We only ever store ONE profile row, so we use a fixed ID = 1.
 * This is a simple pattern for single-row configuration tables.
 */
@Entity(tableName = "profile")
data class ProfileEntity(
    @PrimaryKey
    val id: Long = 1,

    /** User's display name. */
    val name: String = "",

    /** Optional path to the profile image stored locally. */
    val imagePath: String? = null
)
