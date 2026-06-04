package com.decloudius.composetraining.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profile")
data class ProfileEntity(
    @PrimaryKey
    val id: Long = 1,

    val name: String = "",

    val imagePath: String? = null
)
