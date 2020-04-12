package com.cryptox.glovid.viewModels.login

import androidx.lifecycle.LiveData
import com.cryptox.glovid.data.model.User
import com.cryptox.glovid.data.responseModel.ProfileResponse
import com.cryptox.glovid.data.responseModel.UserResponse
import com.cryptox.glovid.network.api.Resource
import com.cryptox.glovid.network.api.ResourceError

interface LoginViewModel {
    fun isLoading():LiveData<Boolean>
    fun getError():LiveData<ResourceError>
    fun login():LiveData<User>
    fun callLoginAPI(username: String, password: String)
    fun processLoginResponse(response: Resource<UserResponse>?)
    fun profile():LiveData<User>
    fun callProfileAPI()
    fun processProfileResponse(response: Resource<ProfileResponse>?)
}