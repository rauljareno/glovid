package com.cryptox.glovid.data.model

/**
 * Data class that captures user information for logged in users retrieved from OrderRepository
 */
data class OrderUser (
    val id: String,
    val email: String,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val phoneNumber: String
)
