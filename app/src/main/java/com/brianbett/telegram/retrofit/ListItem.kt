package com.brianbett.telegram.retrofit

sealed class ListItem<T> {

}
data class ChatItem(val chat: Chat):ListItem<Chat>()
data class ChannelItem(val channelChat: ChannelChat):ListItem<ChannelChat>()
