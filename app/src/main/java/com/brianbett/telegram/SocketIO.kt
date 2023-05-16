package com.brianbett.telegram

import io.socket.client.IO
import io.socket.client.Socket

object SocketIO {
    private const val MY_URL="https://telegram-clone.herokuapp.com"
    private val options = IO.Options().apply {
        secure=true
        reconnection=true
        forceNew=true
    }

    val socket:Socket by lazy{
        IO.socket(MY_URL, options)
    }

}