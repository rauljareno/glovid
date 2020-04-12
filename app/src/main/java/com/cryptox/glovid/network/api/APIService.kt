package com.cryptox.glovid.network.api

import com.cryptox.glovid.data.model.Order
import com.cryptox.glovid.data.responseModel.ProfileResponse
import com.cryptox.glovid.data.responseModel.UserResponse
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface APIService {

    @POST(ApiRoutes.REGISTER)
    fun register(@Body params: RequestBody): Call<UserResponse>

    @POST(ApiRoutes.LOGIN)
    fun login(@Body params: RequestBody): Call<UserResponse>

    @GET(ApiRoutes.PROFILE)
    fun profile(@Header("Authorization") token: String) : Call<ProfileResponse>

    @GET(ApiRoutes.LOGOUT)
    fun logout()

    @POST(ApiRoutes.CREATE_ORDER)
    fun createOrder(@Body params: RequestBody): Call<Order>

    @GET(ApiRoutes.ORDER_LIST)
    fun getOrders(@Body params: RequestBody): Call<List<Order>>

    @GET(ApiRoutes.ORDER_BY_ID)
    fun getOrderById(@Body params: RequestBody): Call<Order>
}