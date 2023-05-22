package com.brianbett.telegram.retrofit

data class Message (val _id:String, val sender:UserDetails,val recipient:UserDetails,val title:String,val createdAt:String,val isRead:Boolean) {
}