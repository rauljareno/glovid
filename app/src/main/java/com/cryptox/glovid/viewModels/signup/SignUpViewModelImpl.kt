package com.cryptox.glovid.viewModels.signup

import android.util.Patterns
import androidx.lifecycle.*
import com.auth0.android.jwt.JWT
import com.cryptox.glovid.R
import com.cryptox.glovid.data.model.User
import com.cryptox.glovid.data.responseModel.UserResponse
import com.cryptox.glovid.network.api.Resource
import com.cryptox.glovid.network.api.ResourceError
import com.cryptox.glovid.repository.UserRepository
import javax.inject.Inject

class SignUpViewModelImpl @Inject constructor(private val userRepository: UserRepository):
    SignUpViewModel, ViewModel(){

    private val isLoading: MutableLiveData<Boolean> = MutableLiveData()
    private val isError: MutableLiveData<Boolean> = MutableLiveData()
    private val isSuccess: MutableLiveData<Boolean> = MutableLiveData()
    private val error: MutableLiveData<ResourceError> = MutableLiveData()
    private val signUpResponse:MutableLiveData<UserResponse> = MutableLiveData()
    private val callObserver: Observer<Resource<UserResponse>> = Observer { t -> processResponse(t) }
    private lateinit var displayName: String
    private lateinit var userId: String

    private val _signUpForm = MutableLiveData<SignUpFormState>()
    val signUpFormState: LiveData<SignUpFormState> = _signUpForm

    override fun callRegisterAPI(name: String, email: String, password: String){
        displayName = name
        userId = email
        userRepository.register(name, email, password).observeForever { callObserver.onChanged(it)}
    }

    override fun processResponse(response: Resource<UserResponse>?){
        when(response?.status){
            Resource.Status.LOADING -> {
                setLoading()
            }
            Resource.Status.SUCCESS -> {
                setSuccess()
                signUpResponse.value = response.data
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

    override fun register(): LiveData<User> {

        return Transformations.map(signUpResponse){
            val jwt = JWT(it?.token!!)
            User(userId, displayName, it?.token!!)
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

    fun signUpDataChanged(name: String, email: String, password: String) {
        if (name.isEmpty()) {
            _signUpForm.value =
                SignUpFormState(nameError = R.string.invalid_name)
        } else if (!isEmailValid(email)) {
            _signUpForm.value =
                SignUpFormState(emailError = R.string.invalid_email)
        } else if (!isPasswordValid(password)) {
            _signUpForm.value =
                SignUpFormState(
                    passwordError = R.string.invalid_password
                )
        } else {
            _signUpForm.value =
                SignUpFormState(isDataValid = true)
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