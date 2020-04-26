package com.cryptox.glovid.viewModels.orders

import androidx.lifecycle.LiveData
import com.cryptox.glovid.data.model.Order
import com.cryptox.glovid.data.responseModel.OrdersResponse
import com.cryptox.glovid.data.responseModel.UserResponse
import com.cryptox.glovid.network.api.Resource
import com.cryptox.glovid.network.api.ResourceError


enum class OrderType {
    ASK,
    GIVE
}

enum class OrderStatus {
    PENDING,
    ACCEPTED,
    ROUTE,
    DELIVERED
}

interface OrdersViewModel {
    fun isLoading():LiveData<Boolean>
    fun getError():LiveData<ResourceError>
    fun createOrder():LiveData<Order>
    fun callCreateOrderAPI(detail: String, type: String)
    fun getUserOrders(orderType: OrderType):LiveData<List<Order>>
    fun callGetUserOrdersAPI()
    fun searchOrders():LiveData<List<Order>>
    fun callSearchOrdersAPI(orderTypeList: List<OrderType>, orderStatusList: List<OrderStatus>)
    fun getUserOrderById():LiveData<Order>
    fun callGetUserOrderByIdAPI(query:String)
    fun processOrdersResponse(response: Resource<List<Order>>?)
    fun processOrderResponse(response: Resource<Order>?)
}