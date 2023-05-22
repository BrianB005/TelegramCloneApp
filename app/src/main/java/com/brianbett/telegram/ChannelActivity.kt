package com.brianbett.telegram

import android.annotation.SuppressLint
import android.app.admin.DelegatedAdminReceiver
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brianbett.telegram.adapters.ChannelMessagesRVAdapter
import com.brianbett.telegram.adapters.MessagesRVAdapter
import com.brianbett.telegram.databinding.ActivityChannelBinding
import com.brianbett.telegram.retrofit.*
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import okhttp3.ResponseBody
import org.json.JSONObject

class ChannelActivity : AppCompatActivity() {
    private lateinit var binding:ActivityChannelBinding
    private lateinit var userId:String
    private lateinit var channelId:String
    private lateinit var title:String
    private lateinit var admin:String
    private lateinit var members:List<String>
    private lateinit var titleInput: AppCompatEditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var messages:ArrayList<ChannelMessage>
    private lateinit var adapter:ChannelMessagesRVAdapter

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityChannelBinding.inflate(layoutInflater)
        setContentView(binding.root)
        channelId=intent.getStringExtra("id")!!
        val name=intent.getStringExtra("name")
        admin=intent.getStringExtra("admin")!!
        val icon=intent.getStringExtra("icon")
        members=intent.getStringArrayListExtra("members")!!
        binding.toolbar.username.text=name
        binding.toolbar.onlineStatus.text="Tap here to view media"


        userId=MyPreferences.getItemFromSP(applicationContext,"userId")!!

        if(members.contains(userId)) {
            binding.join.visibility = View.GONE
            if (admin == userId) {
                binding.input.visibility = View.VISIBLE

            } else {
                binding.input.visibility = View.GONE
            }
        }else{
            binding.input.visibility = View.GONE
            binding.join.visibility = View.VISIBLE
            binding.join.setOnClickListener{
                RetrofitHandler.joinChannel(applicationContext,channelId,object :ResponseBodyInterface{
                    override fun success(response: ResponseBody) {
                        Toast.makeText(applicationContext,"Joined channel successfully!",Toast.LENGTH_LONG).show()
                    }
                    override fun failure(throwable: Throwable) {
                        Toast.makeText(applicationContext,throwable.message,Toast.LENGTH_LONG).show()
                    }

                    override fun errorExists(message: String) {
                        Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
                    }

                })
            }
        }

        binding.toolbar.backIcon.setOnClickListener { onBackPressed() }
        setSupportActionBar(binding.toolbar.root)
        titleInput=binding.messageInput
        recyclerView=binding.recyclerView
        progressBar=binding.progress
        val storageReference = FirebaseStorage.getInstance()
            .getReference("images/${icon}")
        val uriTask: Task<Uri> = storageReference.downloadUrl
        uriTask.addOnSuccessListener { uri1: Uri? ->
            Glide.with(applicationContext).load(uri1).into(binding.toolbar.profilePic)
        }
        binding.send.setOnClickListener{
            title=titleInput.text.toString()
            if(title.isEmpty()){
                titleInput.error="Kindly fill this field!"
            }else {
                val messageData = JSONObject().apply {
                    put("title", title)
                    put("channel", channelId)
                }
                SocketIO.socket.emit("newPost",messageData)
                titleInput.text!!.clear()
            }
        }

        progressBar.visibility= View.VISIBLE
        recyclerView.visibility= View.VISIBLE
        messages=ArrayList()
        adapter= ChannelMessagesRVAdapter(applicationContext,messages)
        recyclerView.adapter=adapter
        recyclerView.layoutManager= LinearLayoutManager(applicationContext)

        RetrofitHandler.getChannelsPosts(applicationContext,channelId,object: ChannelMessagesInterface {
            @SuppressLint("NotifyDataSetChanged")
            override fun success(posts: List<ChannelMessage>) {
                progressBar.visibility= View.GONE
                if(posts.isNotEmpty()){
                    recyclerView.visibility= View.VISIBLE
                    messages.addAll(posts)
                    adapter.notifyDataSetChanged()
                    (recyclerView.layoutManager as LinearLayoutManager).scrollToPosition(adapter.itemCount-1)
                }

            }

            override fun failure(throwable: Throwable) {
                progressBar.visibility= View.GONE
                Toast.makeText(applicationContext,throwable.message, Toast.LENGTH_LONG).show()
            }

            override fun errorExists(message: String) {
                progressBar.visibility= View.GONE
                Toast.makeText(applicationContext,message, Toast.LENGTH_LONG).show()
            }
        })

        SocketIO.socket.on("postReceived"){ args ->
            runOnUiThread {
                val response = args[0] as JSONObject
                Log.d("Post received",response.toString())
                val title=response.getString("title")
                val channelObject=response.getJSONObject("channel")
                val membersArray = channelObject.getJSONArray("members")
                val members = (0 until membersArray.length()).map { membersArray.getString(it) }.toList()

                val channel=ChannelDetails(channelObject.getString("_id"),
                    channelObject.getString("name"),channelObject.getString("admin"),
                    channelObject.getString("icon"), members )
                val createdAt=response.getString("createdAt")
                val id=response.getString("_id")
                val message=ChannelMessage(id,channel,title,createdAt)
                messages.add(message)
                adapter.notifyItemInserted(messages.size)
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.leave->{
                RetrofitHandler.leaveChannel(applicationContext,channelId,
                    object : ResponseBodyInterface {
                        override fun success(response: ResponseBody) {
                            Toast.makeText(applicationContext,"You have left this channel!",Toast.LENGTH_LONG).show()
                            finish()
                        }

                        override fun failure(throwable: Throwable) {

                            Toast.makeText(applicationContext,throwable.message,Toast.LENGTH_LONG).show()
                        }

                        override fun errorExists(message: String) {
                            Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
                        }
                    })
                true

            }R.id.search->{
                true
            }
            else ->super.onOptionsItemSelected(item)


        }

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val leave=menu?.findItem(R.id.leave)
        if(members.contains(userId)) {
            leave?.isVisible = userId != admin
        }
        menuInflater.inflate(R.menu.channel_activity_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }
}