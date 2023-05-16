package com.brianbett.telegram

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brianbett.telegram.adapters.MessagesRVAdapter
import com.brianbett.telegram.databinding.ActivityMessageBinding
import com.brianbett.telegram.retrofit.*
import io.socket.client.Socket
import org.json.JSONObject

class MessageActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMessageBinding
    private lateinit var userId:String
    private lateinit var recipient:String
    private lateinit var title:String
    private lateinit var titleInput:AppCompatEditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var messages:ArrayList<Message>
    private lateinit var adapter:MessagesRVAdapter
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding=ActivityMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        recipient=intent.getStringExtra("recipient")!!
        val phone=intent.getStringExtra("phone")
        binding.toolbar.username.text=phone
        userId=MyPreferences.getItemFromSP(applicationContext,"userId")!!
        SocketIO.socket.connect()
        SocketIO.socket.on(Socket.EVENT_CONNECT) {
            SocketIO.socket.emit("userConnected",userId)
        }

        SocketIO.socket.on(Socket.EVENT_CONNECT_ERROR) {
            Log.d("IOError", "Error: IO Server")
        }

        SocketIO.socket.on("messageReceived"){ args ->
            runOnUiThread {
                val response = args[0] as JSONObject
                val title=response.getString("title")
                val sender=response.getString("sender")
                val recipient=response.getString("recipient")
                val createdAt=response.getString("createdAt")
                val id=response.getString("_id")

                val message=Message(id,sender,recipient,title,createdAt)
                messages.add(message)
                adapter.notifyItemInserted(messages.size)
            }
        }
        binding.toolbar.backIcon.setOnClickListener { onBackPressed() }
        titleInput=binding.messageInput

        recyclerView=binding.recyclerView
        progressBar=binding.progress


        binding.send.setOnClickListener{
            title=titleInput.text.toString()
            if(title.isEmpty()){
                titleInput.error="Kindly fill this field!"
            }else {
                val messageData = JSONObject().apply {
                    put("title", title)
                    put("recipient", recipient)
                    put("sender", userId)
                }
                SocketIO.socket.emit("newMessage",messageData)
                titleInput.text!!.clear()
            }
        }

        progressBar.visibility= View.VISIBLE
        recyclerView.visibility=View.VISIBLE
        messages=ArrayList()
        adapter=MessagesRVAdapter(applicationContext,messages)
        recyclerView.adapter=adapter
        recyclerView.layoutManager=LinearLayoutManager(applicationContext)

        RetrofitHandler.getChat(applicationContext,recipient,object: MessagesInterface {
            @SuppressLint("NotifyDataSetChanged")
            override fun success(messagesList: List<Message>) {
                progressBar.visibility=View.GONE
                if(messagesList.isNotEmpty()){
                    recyclerView.visibility=View.VISIBLE
                    messages.addAll(messagesList)
                    adapter.notifyDataSetChanged()
                    (recyclerView.layoutManager as LinearLayoutManager).scrollToPosition(adapter.itemCount-1)

                }

            }

            override fun failure(throwable: Throwable) {
                progressBar.visibility=View.GONE
                Toast.makeText(applicationContext,throwable.message, Toast.LENGTH_LONG).show()
            }

            override fun errorExists(message: String) {
                progressBar.visibility=View.GONE
                Toast.makeText(applicationContext,message, Toast.LENGTH_LONG).show()
            }

        })

        RetrofitHandler.getUser(applicationContext,recipient,object :UserInterface{
            override fun success(user: UserDetails) {
                val onlineStatus=if(user.online)"Online" else "Last seen ${ConvertToDate.getDateAgoFromDate(user.lastSeen)} " +
                        ConvertToDate.getTimeFromDate(user.lastSeen)
                binding.toolbar.onlineStatus.text=onlineStatus
            }

            override fun failure(throwable: Throwable) {
                Log.e("GetUserE",throwable.message!!)
            }

            override fun errorExists(message: String) {
                Log.e("GetUserE",message)
            }

        })


    }


//    override fun onDestroy() {
//        super.onDestroy()
//        SocketIO.socket.disconnect()
//    }
    override fun onResume() {
        super.onResume()
        SocketIO.socket.connect()
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
    }
}