package com.cryptox.glovid.viewModels.signup

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.util.Patterns
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.cryptox.glovid.R
import com.cryptox.glovid.data.model.User
import com.cryptox.glovid.data.responseModel.UserResponse
import com.cryptox.glovid.network.api.Resource
import com.cryptox.glovid.network.api.ResourceError
import com.cryptox.glovid.repository.UserRepository
import com.google.android.gms.maps.model.LatLng
import java.io.IOException
import java.util.*
import java.util.regex.Pattern
import javax.inject.Inject
import kotlin.collections.ArrayList

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

    override fun callRegisterAPI(name: String, email: String, phoneNumber: String, password: String, location: LatLng, countryCode: String, postalCode: String){
        displayName = name
        userId = email
        userRepository.register(name, email, phoneNumber, location.latitude, location.longitude, countryCode, postalCode, password).observeForever { callObserver.onChanged(it)}
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

        return Transformations.map(signUpResponse) {
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

    fun signUpDataChanged(context: Context, name: String, email: String, phoneNumber: String, password: String, address: String) {
        if (name.isEmpty()) {
            _signUpForm.value =
                SignUpFormState(nameError = R.string.invalid_name)
        } else if (!isEmailValid(email)) {
            _signUpForm.value =
                SignUpFormState(emailError = R.string.invalid_email)
        } else if (!isPhoneNumberValid(phoneNumber)) {
            _signUpForm.value =
                SignUpFormState(phoneNumberError = R.string.invalid_phone_number)
        } else if (!isPasswordValid(password)) {
            _signUpForm.value =
                SignUpFormState(
                    passwordError = R.string.invalid_password
                )
        } else if (!isAddressValid(context, address)) {
            _signUpForm.value =
                SignUpFormState(addressError = R.string.invalid_address)
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

    // A placeholder phone number validation check
    private fun isPhoneNumberValid(phoneNumber: String): Boolean {
        val regex = "[\\+0-9\\(\\)\\- ]{7,19}"
        val pattern: Pattern = Pattern.compile(regex)
        return pattern.matcher(phoneNumber).find()
    }

    private fun isAddressValid(context: Context, address: String): Boolean {
        val geocoder = Geocoder(context, Locale.getDefault())
        if (address.isNotEmpty()) {
            var addresses: List<Address?> = ArrayList()
            try {
                addresses = geocoder.getFromLocationName(address, 5)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            if (addresses.isNotEmpty()) {
                val selectedAddress = addresses[0]
                if (selectedAddress != null) {
                    val latLng = LatLng(selectedAddress.latitude, selectedAddress.longitude)
                    val countryCode = selectedAddress.countryCode
                    val postalCode = selectedAddress.postalCode
                    return countryCode.isNotEmpty() && postalCode.isNotEmpty()
                }
            }
        }
        return false
    }
}