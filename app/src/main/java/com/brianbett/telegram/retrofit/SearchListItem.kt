package com.brianbett.telegram.retrofit

sealed class SearchListItem <T>{
}

data class UserResult(val userSearchResult: UserDetails):SearchListItem<UserDetails>()
data class ChannelResult(val channelDetails: ChannelDetails):SearchListItem<ChannelDetails>()