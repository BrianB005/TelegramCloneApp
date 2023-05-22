package com.brianbett.telegram.retrofit

interface ChannelMessagesInterface {
    fun success(posts: List<ChannelMessage>);
    fun failure(throwable:Throwable)
    fun errorExists(message:String)
}