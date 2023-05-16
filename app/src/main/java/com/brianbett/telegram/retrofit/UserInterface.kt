package com.brianbett.telegram.retrofit

interface UserInterface {
    fun success(user: UserDetails);
    fun failure(throwable:Throwable)
    fun errorExists(message:String)

}