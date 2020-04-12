package com.cryptox.glovid.network.environment

class BaseEnvironment : Environment{

    private var baseUrl = "http://ec2-34-242-28-117.eu-west-1.compute.amazonaws.com:8080/"

    override fun getBaseUrl():String {
      return baseUrl
    }
}