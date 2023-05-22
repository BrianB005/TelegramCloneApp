package com.brianbett.telegram.retrofit

interface ChannelChatsInterface {
    fun success(posts: List<ChannelChat>);
    fun failure(throwable:Throwable)
    fun errorExists(message:String)
}
