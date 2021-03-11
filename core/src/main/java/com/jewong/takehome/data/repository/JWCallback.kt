package com.jewong.takehome.data.repository

interface JWCallback<T> {

    fun onResponse(response: T)

    fun onFailure(throwable: Throwable)
}