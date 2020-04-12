package com.cryptox.glovid.network.api

interface ApiRoutes {

    companion object {
        const val REGISTER = "register"
        const val LOGIN = "session/login"
        const val LOGOUT = "session/logout"
        const val PROFILE = "profile"
        const val CREATE_ORDER = "order"
        const val ORDER_LIST = "order/list"
        const val ORDER_BY_ID = "order/"
    }
}