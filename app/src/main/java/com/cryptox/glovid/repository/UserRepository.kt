package com.cryptox.glovid.repository

import androidx.lifecycle.LiveData
import com.cryptox.glovid.data.responseModel.ProfileResponse
import com.cryptox.glovid.network.api.Resource
import com.cryptox.glovid.data.responseModel.UserResponse

interface UserRepository {

    fun register(name: String, email: String, password: String): LiveData<Resource<UserResponse>>

    fun login(username: String, password: String): LiveData<Resource<UserResponse>>

    fun logout()

    fun profile(): LiveData<Resource<ProfileResponse>>
}