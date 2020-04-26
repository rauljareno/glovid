package com.cryptox.glovid.viewModels.profile

import androidx.lifecycle.*
import com.cryptox.glovid.data.responseModel.ProfileResponse
import com.cryptox.glovid.network.api.Resource
import com.cryptox.glovid.network.api.ResourceError
import com.cryptox.glovid.repository.UserRepository
import javax.inject.Inject

class ProfileViewModelImpl @Inject constructor(private val userRepository: UserRepository):
    ProfileViewModel, ViewModel(){

    private val isLoading: MutableLiveData<Boolean> = MutableLiveData()
    private val isError: MutableLiveData<Boolean> = MutableLiveData()
    private val isSuccess: MutableLiveData<Boolean> = MutableLiveData()
    private val error: MutableLiveData<ResourceError> = MutableLiveData()
    private val profileResponse:MutableLiveData<ProfileResponse> = MutableLiveData()
    private val profileCallObserver: Observer<Resource<ProfileResponse>> = Observer { t -> processProfileResponse(t) }

    override fun callProfileAPI() {
        userRepository.profile().observeForever{ profileCallObserver.onChanged(it) }
    }

    override fun processProfileResponse(response: Resource<ProfileResponse>?){
        when(response?.status){
            Resource.Status.LOADING -> {
                setLoading()
            }
            Resource.Status.SUCCESS -> {
                setSuccess()
                profileResponse.value = response.data
            }
            Resource.Status.ERROR -> {
                setError()
                error.value = response.resourceError
            }
        }
    }

    override fun isLoading(): LiveData<Boolean> {
       return isLoading
    }

    override fun getError(): LiveData<ResourceError> {
        return error
    }

    override fun profile(): LiveData<ProfileResponse> {
        return profileResponse
    }

    fun isError():LiveData<Boolean>{
        return isError
    }

    fun isSuccess():LiveData<Boolean>{
        return isSuccess
    }

    private fun setSuccess(){
        isLoading.value = false
        isSuccess.value = true
        isError.value = false
    }

    private fun setError(){
        isLoading.value = false
        isSuccess.value = false
        isError.value = true
    }

    private fun setLoading(){
        isLoading.value = true
        isSuccess.value = false
        isError.value = false
    }

    public override fun onCleared() {
        super.onCleared()
    }
}