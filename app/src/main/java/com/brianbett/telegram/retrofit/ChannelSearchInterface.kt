package com.brianbett.telegram.retrofit

interface ChannelSearchInterface {
    fun success(channels: List<ChannelDetails>);
    fun failure(throwable:Throwable)
    fun errorExists(message:String)
}