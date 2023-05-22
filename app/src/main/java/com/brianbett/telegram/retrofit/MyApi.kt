package com.brianbett.telegram.retrofit

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface MyApi {
    @POST("auth/register")
    fun register(@Body userDetails: HashMap<String, String>): Call<AuthUser>


    @GET("messages")
    fun getChats(@Header("Authorization")token:String):Call<List<Chat>>
    @GET("messages/{id}")
    fun getChat(@Header("Authorization")token:String,@Path("id") id:String):Call<List<Message>>

    @GET("users/{id}")
    fun getUser(@Header("Authorization")token:String,@Path("id") id:String):Call<UserDetails>
    @GET("users")
    fun getUsers(@Header("Authorization")token:String):Call<List<UserDetails>>

    @GET("posts")
    fun getMyChannelsPosts(@Header("Authorization")token:String):Call<List<ChannelChat>>

    @GET("posts/{id}")
    fun getChannelPosts(@Header("Authorization")token:String,@Path("id") id:String):Call<List<ChannelMessage>>

    @GET("channels")
    fun searchChannels(@Query("channel") channel:String,@Header("Authorization")token:String):Call<List<ChannelDetails>>

    @GET("users/users/search")
    fun searchUsers(@Query("user") user:String,@Header("Authorization")token:String):Call<List<UserDetails>>

    @PUT("channels/join/{id}")
    fun joinChannel(@Header("Authorization")token:String,@Path("id") id:String):Call<ResponseBody>

    @PUT("channels/leave/{id}")
    fun leaveChannel(@Header("Authorization")token:String,@Path("id") id:String):Call<ResponseBody>

    @POST("channels")
    fun createChannel(@Header("Authorization")token:String,@Body channel:HashMap<String,String>):Call<ResponseBody>



}