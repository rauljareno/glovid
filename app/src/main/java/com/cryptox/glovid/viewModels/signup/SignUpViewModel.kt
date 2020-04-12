package com.cryptox.glovid.viewModels.signup

import androidx.lifecycle.LiveData
import com.cryptox.glovid.data.model.User
import com.cryptox.glovid.data.responseModel.UserResponse
import com.cryptox.glovid.network.api.Resource
import com.cryptox.glovid.network.api.ResourceError

interface SignUpViewModel {
    fun isLoading():LiveData<Boolean>
    fun getError():LiveData<ResourceError>
    fun register():LiveData<User>
    fun callRegisterAPI(name: String, email: String, password: String)
    fun processResponse(response: Resource<UserResponse>?)
}