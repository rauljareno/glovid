package com.cryptox.glovid.viewModels.orders

import androidx.lifecycle.LiveData
import com.cryptox.glovid.data.model.Order
import com.cryptox.glovid.network.api.ResourceError


interface OrdersViewModel {
    fun isLoading():LiveData<Boolean>
    fun getError():LiveData<ResourceError>
    fun createOrder():LiveData<Order>
    fun callCreateOrderAPI(id: Int , detail: String, type: String, status: String)
    fun getOrders():LiveData<List<Order>>
    fun callGetOrdersAPI(query:String)
    fun getOrderById():LiveData<Order>
    fun callGetOrderByIdAPI(query:String)
}