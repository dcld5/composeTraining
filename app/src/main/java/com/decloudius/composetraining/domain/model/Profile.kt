package com.decloudius.composetraining.domain.model

data class Profile(
    val id: Long = 1,
    val name: String,
    val imagePath: String?
)
