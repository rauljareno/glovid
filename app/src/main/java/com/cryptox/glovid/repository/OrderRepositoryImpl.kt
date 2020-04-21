package com.cryptox.glovid.repository

import androidx.lifecycle.LiveData
import com.cryptox.glovid.data.model.Order
import com.cryptox.glovid.network.api.APIService
import com.cryptox.glovid.network.api.NetworkCall
import com.cryptox.glovid.network.api.Resource
import com.cryptox.glovid.network.api.ApiRoutes
import com.cryptox.glovid.prefs
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.http.GET
import retrofit2.http.POST
import javax.inject.Inject

class OrderRepositoryImpl @Inject constructor(var apiService: APIService) : OrderRepository {
    private val orderCall = NetworkCall<Order>()
    private val orderListCall = NetworkCall<List<Order>>()

    @POST(ApiRoutes.ORDER_CREATE)
    override fun createOrder(detail: String, type: String):LiveData<Resource<Order>> {
        return orderCall.makeCall(apiService.createOrder("Bearer " + prefs.user?.token, createJsonRequestBody(
            "detail" to detail, "type" to type)))
    }

    @GET(ApiRoutes.USER_ORDERS_LIST)
    override fun getUserOrders(): LiveData<Resource<List<Order>>> {
        return orderListCall.makeCall(apiService.getUserOrders("Bearer " + prefs.user?.token))
    }

    @GET(ApiRoutes.USER_ORDER_BY_ID)
    override fun getUserOrderById(query: String): LiveData<Resource<Order>> {
        return orderCall.makeCall(apiService.getUserOrderById("Bearer " + prefs.user?.token, ""))
    }

    @GET(ApiRoutes.ORDER_SEARCH)
    override fun searchOrders(orderTypeList: String, orderStatusList: String): LiveData<Resource<List<Order>>> {
        return orderListCall.makeCall(apiService.searchOrders("Bearer " + prefs.user?.token, orderTypeList.replace(" ", ""), orderStatusList.replace(" ", "")))
    }

    private fun createJsonRequestBody(vararg params: Pair<String, String>) =
        RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            JSONObject(mapOf(*params)).toString())
}