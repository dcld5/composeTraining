package com.decloudius.composetraining.domain.model

/**
 * Domain model = a plain Kotlin data class with zero Android dependencies.
 * Clean Architecture says: the domain layer should not know about Room, SharedPreferences,
 * or even Android itself. This makes it easy to test and reuse.
 */
data class Photo(
    val id: Long,
    val filePath: String,
    val timestamp: Long
)
