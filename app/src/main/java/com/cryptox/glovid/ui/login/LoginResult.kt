package com.cryptox.glovid.ui.login

import com.cryptox.glovid.data.model.User

/**
 * Authentication result : success (user details) or error message.
 */
data class LoginResult(
        val success: User? = null,
        val error: Int? = null
)
