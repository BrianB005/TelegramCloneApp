package com.brianbett.telegram.retrofit

import android.content.Context
import com.brianbett.telegram.MyPreferences
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
            val userCall: Call<AuthUser> = myApi.register(userDetails);

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
            val messagesCall= myApi.getUser("Bearer $token",id)
            messagesCall.enqueue(object:Callback<UserDetails>{
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
            val messagesCall= myApi.getUsers("Bearer $token")
            messagesCall.enqueue(object:Callback<List<UserDetails>>{
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

    }
}