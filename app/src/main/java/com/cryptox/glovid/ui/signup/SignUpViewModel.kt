package com.cryptox.glovid.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import com.cryptox.glovid.data.Result

import com.cryptox.glovid.R
import com.cryptox.glovid.data.SignUpRepository
import com.cryptox.glovid.ui.login.LoggedInUserView

class SignUpViewModel(private val signUpRepository: SignUpRepository) : ViewModel() {

    private val _signUpForm = MutableLiveData<SignUpFormState>()
    val signUpFormState: LiveData<SignUpFormState> = _signUpForm

    private val _signUpResult = MutableLiveData<SignUpResult>()
    val signUpResult: LiveData<SignUpResult> = _signUpResult

    fun signUp(name: String, email: String, password: String) {
        // can be launched in a separate asynchronous job
        val result = signUpRepository.signup(name, email, password)

        if (result is Result.Success) {
            _signUpResult.value = SignUpResult(
                    success = LoggedInUserView(displayName = result.data.displayName)
            )
        } else {
            _signUpResult.value = SignUpResult(error = R.string.signup_failed)
        }
    }

    fun signUpDataChanged(name: String, email: String, password: String) {
        if (name.isEmpty()) {
            _signUpForm.value = SignUpFormState(nameError = R.string.invalid_name)
        } else if (!isEmailValid(email)) {
            _signUpForm.value = SignUpFormState(emailError = R.string.invalid_email)
        } else if (!isPasswordValid(password)) {
            _signUpForm.value = SignUpFormState(passwordError = R.string.invalid_password)
        } else {
            _signUpForm.value = SignUpFormState(isDataValid = true)
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
        return password.length > 5
    }
}
