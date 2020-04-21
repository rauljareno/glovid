package com.cryptox.glovid.repository

import androidx.lifecycle.LiveData
import com.cryptox.glovid.data.model.Order
import com.cryptox.glovid.data.responseModel.OrdersResponse
import com.cryptox.glovid.network.api.Resource

interface OrderRepository {

    fun createOrder(detail: String, type: String): LiveData<Resource<Order>>

    fun getUserOrders(): LiveData<Resource<List<Order>>>

    fun getUserOrderById(query: String): LiveData<Resource<Order>>

    fun searchOrders(orderTypeList: String, orderStatusList: String): LiveData<Resource<List<Order>>>
}