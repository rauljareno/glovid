package com.cryptox.glovid.network.api

interface ApiRoutes {

    companion object {
        const val REGISTER = "register"
        const val LOGIN = "session/login"
        const val LOGOUT = "session/logout"
        const val PROFILE = "profile"
        const val ORDER_CREATE = "order"
        const val USER_ORDERS_LIST = "order/list"
        const val ORDER_SEARCH = "order/search"
        const val USER_ORDER_BY_ID = "order/"
    }
}