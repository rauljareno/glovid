package com.cryptox.glovid.data.model

/**
 * Data class that captures order information retrieved from OrderRepository
 */
data class Order (
        val id: Int,
        val detail: String,
        val type: String,
        val status: String,
        val user: OrderUser?
)
