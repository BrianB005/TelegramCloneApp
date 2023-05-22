package com.brianbett.telegram.retrofit

import com.google.gson.annotations.SerializedName

data class ChannelDetails(@SerializedName("_id") val id:String,val  name:String,val admin:String,val icon:String,val members:List<String>) {
}