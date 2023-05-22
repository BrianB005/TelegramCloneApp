package com.brianbett.telegram.retrofit

import okhttp3.ResponseBody

interface ResponseBodyInterface {
    fun success(response: ResponseBody)
    fun failure(throwable: Throwable)
    fun errorExists(message:String)
}