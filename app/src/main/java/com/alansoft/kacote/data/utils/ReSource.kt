package com.alansoft.kacote.data.utils

/**
 * Created by LEE MIN KYU on 2021/03/24
 * Copyright © 2021 Dreamus Company. All rights reserved.
 */
data class Resource<out T>(val status: Status, val data: T?, val message: String?) {
    enum class Status { SUCCESS, ERROR, LOADING }
    companion object {
        fun <T> success(data: T): Resource<T> = Resource(Status.SUCCESS, data, null)
        fun <T> error(message: String, data: T? = null): Resource<T> =
            Resource(Status.ERROR, data, message)

        fun <T> loading(data: T? = null): Resource<T> = Resource(Status.LOADING, data, null)
    }
}
