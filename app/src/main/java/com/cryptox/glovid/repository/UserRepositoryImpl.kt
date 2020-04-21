package com.cryptox.glovid.repository

import androidx.lifecycle.LiveData
import com.cryptox.glovid.data.responseModel.ProfileResponse
import com.cryptox.glovid.data.responseModel.UserResponse
import com.cryptox.glovid.network.api.APIService
import com.cryptox.glovid.network.api.NetworkCall
import com.cryptox.glovid.network.api.Resource
import com.cryptox.glovid.network.api.ApiRoutes
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.http.POST
import javax.inject.Inject
import com.cryptox.glovid.prefs

class UserRepositoryImpl @Inject constructor(var apiService: APIService) : UserRepository {
    private val userCall = NetworkCall<UserResponse>()
    private val profileCall = NetworkCall<ProfileResponse>()

    @POST(ApiRoutes.REGISTER)
    override fun register(name: String, email: String, phoneNumber: String, latitude: Double,
                          longitude: Double, countryCode: String, postalCode: String,
                          password: String):LiveData<Resource<UserResponse>> {
       return userCall.makeCall(apiService.register(createJsonRequestBody(
           "name" to name, "email" to email, "phoneNumber" to phoneNumber,
           "latitude" to latitude.toString(), "longitude" to longitude.toString(),
           "countryCode" to countryCode, "postalCode" to postalCode, "password" to password)))
    }

    @POST(ApiRoutes.LOGIN)
    override fun login(username: String, password: String):LiveData<Resource<UserResponse>> {
        return userCall.makeCall(apiService.login(createJsonRequestBody(
            "username" to username, "password" to password)))
    }

    override fun logout() {
        TODO("Not yet implemented")
    }

    override fun profile(): LiveData<Resource<ProfileResponse>> {
        return profileCall.makeCall(apiService.profile("Bearer " + prefs.user?.token))
    }

    private fun createJsonRequestBody(vararg params: Pair<String, String>) =
        RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            JSONObject(mapOf(*params)).toString())
}