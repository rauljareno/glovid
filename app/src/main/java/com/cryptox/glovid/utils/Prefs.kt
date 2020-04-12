package com.cryptox.glovid.utils

import android.content.Context
import android.content.SharedPreferences
import com.cryptox.glovid.data.model.User
import com.google.gson.Gson




class Prefs (context: Context) {
    val PREFS_FILENAME = "com.cryptox.glovid.prefs"
    val USER = "user"
    val ONBOARDING = "onboarding"
    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0);

    var user: User?
        get() {
            val json: String? = prefs.getString(USER, "")
            return Gson().fromJson(json, User::class.java)
        }
        set(value) {
            val json = Gson().toJson(value)
            prefs.edit().putString(USER, json).apply()
        }

    var onboarding: Boolean
        get() {
            return prefs.getBoolean(ONBOARDING, false)
        }
        set(value) {
            prefs.edit().putBoolean(ONBOARDING, value).apply()
        }
}