package com.cryptox.glovid.ui.signup

/**
 * Data validation state of the sign up form.
 */
data class SignUpFormState(val nameError: Int? = null,
                           val emailError: Int? = null,
                           val passwordError: Int? = null,
                           val isDataValid: Boolean = false)
