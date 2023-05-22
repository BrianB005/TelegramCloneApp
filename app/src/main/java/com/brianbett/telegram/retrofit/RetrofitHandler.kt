package com.brianbett.telegram.retrofit

import android.content.Context
import com.brianbett.telegram.MyPreferences
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class RetrofitHandler {
    companion object{
        private val retrofit=Retrofit.Builder().baseUrl("https://telegram-clone.herokuapp.com/api/v1/").addConverterFactory(GsonConverterFactory.create()).build()
        private val myApi=retrofit.create(MyApi::class.java)

        fun register(userDetails:HashMap<String,String>,userInterface: AuthUserInterface) {
            val userCall: Call<AuthUser> = myApi.register(userDetails)

            userCall.enqueue(object : Callback<AuthUser> {
                override fun onResponse(call: Call<AuthUser>, response: Response<AuthUser>) {
                    if (!response.isSuccessful) {
                        try {

                            val jsonObject = JSONObject(response.errorBody()!!.string())
                            val errorMessage = jsonObject.getString("msg")
                            userInterface.errorExists(errorMessage)
                        } catch (e: IOException) {
                            e.printStackTrace()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    } else {
                        userInterface.success(response.body()!!)
                    }
                }

                override fun onFailure(call: Call<AuthUser>, t: Throwable) {
                    userInterface.failure(t)
                }
            })
        }
        fun getChats(context: Context, messagesInterface: ChatsInterface){
            val token= MyPreferences.getItemFromSP(context,"token")!!
            val messagesCall= myApi.getChats("Bearer $token")
            messagesCall.enqueue(object:Callback<List<Chat>>{
                override fun onResponse(
                    call: Call<List<Chat>>,
                    response: Response<List<Chat>>
                ) {
                    if (!response.isSuccessful) {
                        try {

                            val jsonObject = JSONObject(response.errorBody()!!.string())
                            val errorMessage = jsonObject.getString("msg")
                            messagesInterface.errorExists(errorMessage)
                        } catch (e: IOException) {
                            e.printStackTrace()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    } else {
                        messagesInterface.success(response.body()!!)
                    }
                }

                override fun onFailure(call: Call<List<Chat>>, t: Throwable) {
                    messagesInterface.failure(t)
                }
            })
        }
        fun getChat(context: Context,id:String ,messagesInterface: MessagesInterface){
            val token= MyPreferences.getItemFromSP(context,"token")!!
            val messagesCall= myApi.getChat("Bearer $token",id)
            messagesCall.enqueue(object:Callback<List<Message>>{
                override fun onResponse(
                    call: Call<List<Message>>,
                    response: Response<List<Message>>
                ) {
                    if (!response.isSuccessful) {
                        try {

                            val jsonObject = JSONObject(response.errorBody()!!.string())
                            val errorMessage = jsonObject.getString("msg")
                            messagesInterface.errorExists(errorMessage)
                        } catch (e: IOException) {
                            e.printStackTrace()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    } else {
                        messagesInterface.success(response.body()!!)
                    }
                }

                override fun onFailure(call: Call<List<Message>>, t: Throwable) {
                    messagesInterface.failure(t)
                }
            })
        }

        fun getUser(context: Context,id:String ,userInterface: UserInterface){
            val token= MyPreferences.getItemFromSP(context,"token")!!
            val userCall= myApi.getUser("Bearer $token",id)
            userCall.enqueue(object:Callback<UserDetails>{
                override fun onResponse(
                    call: Call<UserDetails>,
                    response: Response<UserDetails>
                ) {
                    if (!response.isSuccessful) {
                        try {

                            val jsonObject = JSONObject(response.errorBody()!!.string())
                            val errorMessage = jsonObject.getString("msg")
                            userInterface.errorExists(errorMessage)
                        } catch (e: IOException) {
                            e.printStackTrace()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    } else {
                        userInterface.success(response.body()!!)
                    }
                }
                override fun onFailure(call: Call<UserDetails>, t: Throwable) {
                    userInterface.failure(t)
                }
            })
        }
        fun getUsers(context: Context ,usersInterface: UsersInterface){
            val token= MyPreferences.getItemFromSP(context,"token")!!
            val usersCall= myApi.getUsers("Bearer $token")
            usersCall.enqueue(object:Callback<List<UserDetails>>{
                override fun onResponse(
                    call: Call<List<UserDetails>>,
                    response: Response<List<UserDetails>>
                ) {
                    if (!response.isSuccessful) {
                        try {

                            val jsonObject = JSONObject(response.errorBody()!!.string())
                            val errorMessage = jsonObject.getString("msg")
                            usersInterface.errorExists(errorMessage)
                        } catch (e: IOException) {
                            e.printStackTrace()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    } else {
                        usersInterface.success(response.body()!!)
                    }
                }
                override fun onFailure(call: Call<List<UserDetails>>, t: Throwable) {
                    usersInterface.failure(t)
                }
            })
        }
        fun getMyChannelsPosts(context: Context ,channelChatsInterface: ChannelChatsInterface){
            val token= MyPreferences.getItemFromSP(context,"token")!!
            val messagesCall= myApi.getMyChannelsPosts("Bearer $token")
            messagesCall.enqueue(object:Callback<List<ChannelChat>>{
                override fun onResponse(
                    call: Call<List<ChannelChat>>,
                    response: Response<List<ChannelChat>>
                ) {
                    if (!response.isSuccessful) {
                        try {

                            val jsonObject = JSONObject(response.errorBody()!!.string())
                            val errorMessage = jsonObject.getString("msg")
                            channelChatsInterface.errorExists(errorMessage)
                        } catch (e: IOException) {
                            e.printStackTrace()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    } else {
                        channelChatsInterface.success(response.body()!!)
                    }
                }
                override fun onFailure(call: Call<List<ChannelChat>>, t: Throwable) {
                    channelChatsInterface.failure(t)
                }
            })
        }

        fun getChannelsPosts(context: Context ,id:String,channelChatsInterface: ChannelMessagesInterface){
            val token= MyPreferences.getItemFromSP(context,"token")!!
            val messagesCall= myApi.getChannelPosts("Bearer $token",id)
            messagesCall.enqueue(object:Callback<List<ChannelMessage>>{
                override fun onResponse(
                    call: Call<List<ChannelMessage>>,
                    response: Response<List<ChannelMessage>>
                ) {
                    if (!response.isSuccessful) {
                        try {

                            val jsonObject = JSONObject(response.errorBody()!!.string())
                            val errorMessage = jsonObject.getString("msg")
                            channelChatsInterface.errorExists(errorMessage)
                        } catch (e: IOException) {
                            e.printStackTrace()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    } else {
                        channelChatsInterface.success(response.body()!!)
                    }
                }
                override fun onFailure(call: Call<List<ChannelMessage>>, t: Throwable) {
                    channelChatsInterface.failure(t)
                }
            })
        }

        fun searchChannels(context: Context ,channel:String,channelSearchInterface: ChannelSearchInterface){
            val token= MyPreferences.getItemFromSP(context,"token")!!
            val channelsCall= myApi.searchChannels(channel ,"Bearer $token",)
            channelsCall.enqueue(object:Callback<List<ChannelDetails>>{
                override fun onResponse(
                    call: Call<List<ChannelDetails>>,
                    response: Response<List<ChannelDetails>>
                ) {
                    if (!response.isSuccessful) {
                        try {

                            val jsonObject = JSONObject(response.errorBody()!!.string())
                            val errorMessage = jsonObject.getString("msg")
                            channelSearchInterface.errorExists(errorMessage)
                        } catch (e: IOException) {
                            e.printStackTrace()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    } else {
                        channelSearchInterface.success(response.body()!!)
                    }
                }
                override fun onFailure(call: Call<List<ChannelDetails>>, t: Throwable) {
                    channelSearchInterface.failure(t)
                }
            })
        }
        fun searchUsers(context: Context ,query:String,userSearchInterface: UserSearchInterface){
            val token= MyPreferences.getItemFromSP(context,"token")!!
            val channelsCall= myApi.searchUsers(query,"Bearer $token",)
            channelsCall.enqueue(object:Callback<List<UserDetails>>{
                override fun onResponse(
                    call: Call<List<UserDetails>>,
                    response: Response<List<UserDetails>>
                ) {
                    if (!response.isSuccessful) {
                        try {

                            val jsonObject = JSONObject(response.errorBody()!!.string())
                            val errorMessage = jsonObject.getString("msg")
                            userSearchInterface.errorExists(errorMessage)
                        } catch (e: IOException) {
                            e.printStackTrace()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    } else {
                        userSearchInterface.success(response.body()!!)
                    }
                }
                override fun onFailure(call: Call<List<UserDetails>>, t: Throwable) {
                    userSearchInterface.failure(t)
                }
            })
        }
        fun joinChannel(context: Context ,id:String,responseBodyInterface: ResponseBodyInterface){
            val token= MyPreferences.getItemFromSP(context,"token")!!
            val channelsCall= myApi.joinChannel("Bearer $token",id)
            channelsCall.enqueue(object:Callback<ResponseBody>{
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (!response.isSuccessful) {
                        try {
                            val jsonObject = JSONObject(response.errorBody()!!.string())
                            val errorMessage = jsonObject.getString("msg")
                            responseBodyInterface.errorExists(errorMessage)
                        } catch (e: IOException) {
                            e.printStackTrace()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    } else {
                        responseBodyInterface.success(response.body()!!)
                    }
                }
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    responseBodyInterface.failure(t)
                }
            })
        }

        fun leaveChannel(context: Context ,id:String,responseBodyInterface: ResponseBodyInterface){
            val token= MyPreferences.getItemFromSP(context,"token")!!
            val channelsCall= myApi.leaveChannel("Bearer $token",id)
            channelsCall.enqueue(object:Callback<ResponseBody>{
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (!response.isSuccessful) {
                        try {
                            val jsonObject = JSONObject(response.errorBody()!!.string())
                            val errorMessage = jsonObject.getString("msg")
                            responseBodyInterface.errorExists(errorMessage)
                        } catch (e: IOException) {
                            e.printStackTrace()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    } else {
                        responseBodyInterface.success(response.body()!!)
                    }
                }
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    responseBodyInterface.failure(t)
                }
            })
        }
        fun createChannel(context: Context ,channel: HashMap<String,String>,responseBodyInterface: ResponseBodyInterface){
            val token= MyPreferences.getItemFromSP(context,"token")!!
            val channelsCall= myApi.createChannel("Bearer $token",channel)
            channelsCall.enqueue(object:Callback<ResponseBody>{
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (!response.isSuccessful) {
                        try {
                            val jsonObject = JSONObject(response.errorBody()!!.string())
                            val errorMessage = jsonObject.getString("msg")
                            responseBodyInterface.errorExists(errorMessage)
                        } catch (e: IOException) {
                            e.printStackTrace()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    } else {
                        responseBodyInterface.success(response.body()!!)
                    }
                }
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    responseBodyInterface.failure(t)
                }
            })
        }

    }
}