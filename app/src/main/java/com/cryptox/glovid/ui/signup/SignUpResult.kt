package com.cryptox.glovid.ui.signup

import com.cryptox.glovid.ui.login.LoggedInUserView

/**
 * Authentication result : success (user details) or error message.
 */
data class SignUpResult(
        val success: LoggedInUserView? = null,
        val error: Int? = null
)
