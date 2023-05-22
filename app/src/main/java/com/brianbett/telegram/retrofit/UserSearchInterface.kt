package com.brianbett.telegram.retrofit

interface UserSearchInterface {
    fun success(users: List<UserDetails>);
    fun failure(throwable:Throwable)
    fun errorExists(message:String)
}