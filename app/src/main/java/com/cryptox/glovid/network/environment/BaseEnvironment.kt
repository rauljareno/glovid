package com.cryptox.glovid.network.environment

import com.cryptox.glovid.BuildConfig

class BaseEnvironment : Environment{

    private var baseUrl = BuildConfig.BASE_URL

    override fun getBaseUrl():String {
      return baseUrl
    }
}