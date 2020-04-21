package com.cryptox.glovid.repository

import android.location.Location
import androidx.lifecycle.LiveData
import com.cryptox.glovid.data.responseModel.ProfileResponse
import com.cryptox.glovid.network.api.Resource
import com.cryptox.glovid.data.responseModel.UserResponse

interface UserRepository {

    fun register(name: String, email: String, phoneNumber: String, latitude: Double,
                 longitude: Double, countryCode: String, postalCode: String, password: String): LiveData<Resource<UserResponse>>

    fun login(username: String, password: String): LiveData<Resource<UserResponse>>

    fun logout()

    fun profile(): LiveData<Resource<ProfileResponse>>
}