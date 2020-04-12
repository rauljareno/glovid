package com.cryptox.glovid.network.http

import com.cryptox.glovid.network.api.APIService
import retrofit2.Retrofit

interface HttpClient {
    fun getApiService() : APIService
    fun getRetrofit(): Retrofit
}