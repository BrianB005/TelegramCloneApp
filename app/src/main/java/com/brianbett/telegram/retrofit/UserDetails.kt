package com.brianbett.telegram.retrofit

import com.google.gson.annotations.SerializedName
import java.util.*

class UserDetails(@SerializedName("_id")  val userId:String,
                  val username:String, val lastSeen: Date, val online:Boolean, val phoneNumber:String, val profilePic:String) {
}