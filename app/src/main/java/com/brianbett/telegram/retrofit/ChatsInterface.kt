package com.brianbett.telegram.retrofit

interface ChatsInterface {
    fun success(chats: List<Chat>);
    fun failure(throwable:Throwable)
    fun errorExists(message:String)
}