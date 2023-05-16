package com.brianbett.telegram.retrofit

interface MessagesInterface {
    fun success(messagesList: List<Message>);
    fun failure(throwable:Throwable)
    fun errorExists(message:String)
}