package com.cryptox.glovid.data.model

/**
 * Data class that captures order information retrieved from OrderRepository
 */
data class Order (
        val id: String,
        val detail: String,
        val status: String,
        val type: Int
)
