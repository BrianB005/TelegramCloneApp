package com.brianbett.telegram.retrofit

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


}