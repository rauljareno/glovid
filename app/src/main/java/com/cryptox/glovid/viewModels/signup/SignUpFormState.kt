package com.cryptox.glovid.viewModels.signup

/**
 * Data validation state of the sign up form.
 */
data class SignUpFormState(val nameError: Int? = null,
                           val emailError: Int? = null,
                           val phoneNumberError: Int? = null,
                           val passwordError: Int? = null,
                           val addressError: Int? = null,
                           val isDataValid: Boolean = false)
