package com.cryptox.glovid.network.api

import com.cryptox.glovid.data.model.Order
import com.cryptox.glovid.data.responseModel.OrdersResponse
import com.cryptox.glovid.data.responseModel.ProfileResponse
import com.cryptox.glovid.data.responseModel.UserResponse
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*

interface APIService {

    @POST(ApiRoutes.REGISTER)
    fun register(@Body params: RequestBody): Call<UserResponse>

    @POST(ApiRoutes.LOGIN)
    fun login(@Body params: RequestBody): Call<UserResponse>

    @GET(ApiRoutes.PROFILE)
    fun profile(@Header("Authorization") token: String) : Call<ProfileResponse>

    @GET(ApiRoutes.LOGOUT)
    fun logout()

    @POST(ApiRoutes.ORDER_CREATE)
    fun createOrder(@Header("Authorization") token: String, @Body params: RequestBody): Call<Order>

    @GET(ApiRoutes.USER_ORDERS_LIST)
    fun getUserOrders(@Header("Authorization") token: String): Call<List<Order>>

    @GET(ApiRoutes.ORDER_SEARCH)
    fun searchOrders(@Header("Authorization") token: String,
                     @Query("orderTypeList", encoded = true) orderTypeList: String,
                     @Query("orderStatusList", encoded = true) orderStatusList: String): Call<List<Order>>

    @GET(ApiRoutes.USER_ORDER_BY_ID)
    fun getUserOrderById(@Header("Authorization") token: String,
                         @Query("") id: String): Call<Order>
}