package com.cryptox.glovid.viewModels.profile

import androidx.lifecycle.LiveData
import com.cryptox.glovid.data.model.User
import com.cryptox.glovid.data.responseModel.ProfileResponse
import com.cryptox.glovid.data.responseModel.UserResponse
import com.cryptox.glovid.network.api.Resource
import com.cryptox.glovid.network.api.ResourceError
import com.google.android.gms.maps.model.LatLng

interface ProfileViewModel {
    fun isLoading():LiveData<Boolean>
    fun getError():LiveData<ResourceError>
    fun profile():LiveData<ProfileResponse>
    fun callProfileAPI()
    fun processProfileResponse(response: Resource<ProfileResponse>?)
}