package com.cryptox.glovid.repository

import androidx.lifecycle.LiveData
import com.cryptox.glovid.data.model.Order
import com.cryptox.glovid.data.responseModel.UserResponse
import com.cryptox.glovid.network.api.APIService
import com.cryptox.glovid.network.api.NetworkCall
import com.cryptox.glovid.network.api.Resource
import com.cryptox.glovid.repository.UserRepository
//import shasin.weatherapp.data.responseModel.WeatherForecastResponse
import com.cryptox.glovid.network.api.ApiRoutes
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.http.GET
import retrofit2.http.POST
import javax.inject.Inject

class OrderRepositoryImpl @Inject constructor(var apiService: APIService) : OrderRepository {
    private val userCall = NetworkCall<Order>()

    @POST(ApiRoutes.CREATE_ORDER)
    override fun createOrder(id: Int , detail: String, type: String, status: String):LiveData<Resource<Order>> {
        return userCall.makeCall(apiService.createOrder(createJsonRequestBody(
            "id" to id.toString(), "detail" to detail, "type" to type, "status" to status)))
    }

    @GET(ApiRoutes.ORDER_LIST)
    override fun getOrders(query: String): LiveData<Resource<List<Order>>> {
        TODO("Not yet implemented")
    }

    @GET(ApiRoutes.ORDER_BY_ID)
    override fun getOrderById(query: String): LiveData<Resource<Order>> {
        TODO("Not yet implemented")
    }

    private fun createJsonRequestBody(vararg params: Pair<String, String>) =
        RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            JSONObject(mapOf(*params)).toString())
}