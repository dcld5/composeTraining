package com.decloudius.composetraining.domain.model

/**
 * Domain model for the user profile.
 * Keeping it separate from ProfileEntity means we could swap Room for
 * another database later without touching the UI code.
 */
data class Profile(
    val id: Long = 1,
    val name: String,
    val imagePath: String?
)
