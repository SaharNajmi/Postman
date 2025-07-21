package com.example.postman.common.extensions

fun Exception.getNetworkErrorMessage(): String {
    return when (this) {
        is java.net.UnknownHostException -> "Unknown Host"
        is java.net.SocketTimeoutException -> "Connection timed out"
        is java.net.ConnectException -> "Couldn't connect to the server"
        is retrofit2.HttpException -> {
            val code = this.code()
            when (code) {
                400 -> "Bad request."
                401 -> "You are not authorized."
                403 -> "Access denied."
                404 -> "Not found."
                405 -> "This operation isn’t allowed."
                500 -> "Server error. Please try again later."
                else -> "HTTP error: $code"
            }
        }

        else -> "Unexpected error: ${this.localizedMessage}"
    }
}