package com.brianbett.telegram

import android.annotation.SuppressLint
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brianbett.telegram.adapters.MessagesRVAdapter
import com.brianbett.telegram.databinding.ActivityMessageBinding
import com.brianbett.telegram.retrofit.*
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.storage.FirebaseStorage
import io.socket.client.Socket
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

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
        binding = ActivityMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        recipient = intent.getStringExtra("recipient")!!
        val phone = intent.getStringExtra("phone")
        binding.toolbar.username.text = phone
        userId = MyPreferences.getItemFromSP(applicationContext, "userId")!!

        SocketIO.socket.on("messageReceived") { args ->
            runOnUiThread {
                val response = args[0] as JSONObject
                val title = response.getString("title")
                val senderObject = response.getJSONObject("sender")
                val recipientObject = response.getJSONObject("recipient")
                val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault())
                val sender = UserDetails(
                    senderObject.getString("_id"),
                    senderObject.getString("username"),
                    dateFormat.parse(senderObject.getString("lastSeen"))!!,
                    senderObject.getBoolean("online"),
                    senderObject.getString("phoneNumber"),
                    senderObject.getString("profilePic")
                )
                val recipient = UserDetails(
                    recipientObject.getString("_id"),
                    recipientObject.getString("username"),
                    dateFormat.parse(recipientObject.getString("lastSeen"))!!,
                    recipientObject.getBoolean("online"),
                    recipientObject.getString("phoneNumber"),
                    recipientObject.getString("profilePic")
                )
                val createdAt = response.getString("createdAt")
                val id = response.getString("_id")
                val message =
                    Message(id, sender, recipient, title, createdAt, response.getBoolean("isRead"))
                messages.add(message)
                adapter.notifyItemInserted(messages.size)
            }
        }
        binding.toolbar.backIcon.setOnClickListener { onBackPressed() }
        titleInput = binding.messageInput

        recyclerView = binding.recyclerView
        progressBar = binding.progress


        binding.send.setOnClickListener {
            title = titleInput.text.toString()
            if (title.isEmpty()) {
                titleInput.error = "Kindly fill this field!"
            } else {
                val messageData = JSONObject().apply {
                    put("title", title)
                    put("recipient", recipient)
                    put("sender", userId)
                }
                SocketIO.socket.emit("newMessage", messageData)
                titleInput.text!!.clear()
            }
        }
        val toolbar=binding.toolbar
        setSupportActionBar(toolbar.root)

        progressBar.visibility = View.VISIBLE
        recyclerView.visibility = View.VISIBLE
        messages = ArrayList()
        adapter = MessagesRVAdapter(applicationContext, messages)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)

        RetrofitHandler.getChat(applicationContext, recipient, object : MessagesInterface {
            @SuppressLint("NotifyDataSetChanged")
            override fun success(messagesList: List<Message>) {
                progressBar.visibility = View.GONE
                if (messagesList.isNotEmpty()) {
                    recyclerView.visibility = View.VISIBLE
                    messages.addAll(messagesList)
                    adapter.notifyDataSetChanged()
                    (recyclerView.layoutManager as LinearLayoutManager).scrollToPosition(adapter.itemCount - 1)

                }

            }

            override fun failure(throwable: Throwable) {
                progressBar.visibility = View.GONE
                Toast.makeText(applicationContext, throwable.message, Toast.LENGTH_LONG).show()
            }

            override fun errorExists(message: String) {
                progressBar.visibility = View.GONE
                Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
            }
        })

        RetrofitHandler.getUser(applicationContext, recipient, object : UserInterface {
            override fun success(user: UserDetails) {
                val onlineStatus = if (user.online) "Online" else "Last seen ${
                    ConvertToDate.getDateAgoFromDate(user.lastSeen)
                } " +
                        ConvertToDate.getTimeFromDate(user.lastSeen)
                binding.toolbar.onlineStatus.text = onlineStatus
                val storageReference = FirebaseStorage.getInstance()
                    .getReference("images/${user.profilePic}")
                val uriTask: Task<Uri> = storageReference.downloadUrl
                uriTask.addOnSuccessListener { uri1: Uri? ->
                    Glide.with(applicationContext).load(uri1).into(binding.toolbar.profilePic)
                }
            }

            override fun failure(throwable: Throwable) {
                Log.e("GetUserE", throwable.message!!)
            }

            override fun errorExists(message: String) {
                Log.e("GetUserE", message)
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
        menuInflater.inflate(R.menu.message_activity_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }
}