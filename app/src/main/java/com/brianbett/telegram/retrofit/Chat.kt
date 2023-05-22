package com.brianbett.telegram.retrofit


data class Chat (val _id:String, val user:UserDetails,val title:String,val createdAt:String,val isRead:Boolean,val recipient:String) {
}
