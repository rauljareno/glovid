package com.cryptox.glovid.viewModels.login

import android.util.Patterns
import androidx.lifecycle.*
import com.auth0.android.jwt.JWT
import com.cryptox.glovid.R
import com.cryptox.glovid.data.model.User
import com.cryptox.glovid.data.responseModel.ProfileResponse
import com.cryptox.glovid.data.responseModel.UserResponse
import com.cryptox.glovid.network.api.Resource
import com.cryptox.glovid.network.api.ResourceError
import com.cryptox.glovid.prefs
import com.cryptox.glovid.repository.UserRepository
import javax.inject.Inject

class LoginViewModelImpl @Inject constructor(private val userRepository: UserRepository):
    LoginViewModel, ViewModel(){

    private val isLoading: MutableLiveData<Boolean> = MutableLiveData()
    private val isError: MutableLiveData<Boolean> = MutableLiveData()
    private val isSuccess: MutableLiveData<Boolean> = MutableLiveData()
    private val error: MutableLiveData<ResourceError> = MutableLiveData()
    private val loginResponse:MutableLiveData<UserResponse> = MutableLiveData()
    private val loginCallObserver: Observer<Resource<UserResponse>> = Observer { t -> processLoginResponse(t) }
    private val profileResponse:MutableLiveData<ProfileResponse> = MutableLiveData()
    private val profileCallObserver: Observer<Resource<ProfileResponse>> = Observer { t -> processProfileResponse(t) }
    private lateinit var userId: String

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    override fun callLoginAPI(username: String, password: String){
        userId = username
        userRepository.login(username, password).observeForever { loginCallObserver.onChanged(it)}
    }

    override fun callProfileAPI() {
        userRepository.profile().observeForever{ profileCallObserver.onChanged(it) }
    }

    override fun processLoginResponse(response: Resource<UserResponse>?){
        when(response?.status){
            Resource.Status.LOADING -> {
                setLoading()
            }
            Resource.Status.SUCCESS -> {
                setSuccess()
                loginResponse.value = response.data
            }
            Resource.Status.ERROR -> {
                setError()
                error.value = response.resourceError
            }
        }
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

    override fun login(): LiveData<User> {
        return Transformations.map(loginResponse){
            val jwt = JWT(it?.token!!)
            User(userId, "", it.token!!)
        }
    }

    override fun profile(): LiveData<User> {
        return Transformations.map(profileResponse){
            User(it.email!!, it.name!!, prefs.user!!.token)
        }
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

    fun loginDataChanged(username: String, password: String) {
       if (!isEmailValid(username)) {
           _loginForm.value =
                LoginFormState(usernameError = R.string.invalid_email)
        } else if (!isPasswordValid(password)) {
           _loginForm.value =
               LoginFormState(
                    passwordError = R.string.invalid_password
                )
        } else {
           _loginForm.value =
                LoginFormState(isDataValid = true)
        }
    }

    // A placeholder email validation check
    private fun isEmailValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.isNotEmpty()
    }
}