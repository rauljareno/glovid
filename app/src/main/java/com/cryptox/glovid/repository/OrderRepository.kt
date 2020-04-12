package com.cryptox.glovid.repository

import androidx.lifecycle.LiveData
import com.cryptox.glovid.data.model.Order
import com.cryptox.glovid.network.api.Resource

interface OrderRepository {

    fun createOrder(id: Int , detail: String, type: String, status: String): LiveData<Resource<Order>>

    fun getOrders(query: String): LiveData<Resource<List<Order>>>

    fun getOrderById(query: String): LiveData<Resource<Order>>
}