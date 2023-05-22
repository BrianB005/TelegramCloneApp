package com.brianbett.telegram.adapters


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.brianbett.telegram.*
import com.brianbett.telegram.databinding.SingleChatBinding
import com.brianbett.telegram.retrofit.ChannelItem
import com.brianbett.telegram.retrofit.Chat
import com.brianbett.telegram.retrofit.ChatItem
import com.brianbett.telegram.retrofit.ListItem
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage

class ChatsRVAdapter(private var chats:MutableList<ListItem<*>>, val context:Context) :RecyclerView.Adapter<ChatsRVAdapter.MyViewHolder>(){

    init {
        sortItems()
    }
    class MyViewHolder(binding: SingleChatBinding):RecyclerView.ViewHolder(binding.root){
        val name=binding.profileName
        val profilePic=binding.profilePic
        val title=binding.chatTitle
        val time=binding.time
        val read=binding.read
        val sent=binding.sent
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val   chatBinding=SingleChatBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(chatBinding)

    }



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val chat = chats[position]
        val userId=MyPreferences.getItemFromSP(context,"userId")
        if(chat is ChatItem) {
            val userChat=chat.chat

            holder.time.text = ConvertToDate.getDateAgo(userChat.createdAt)
            holder.name.text = userChat.user.phoneNumber
            holder.title.text = userChat.title
            val storageReference = FirebaseStorage.getInstance()
                .getReference("images/${userChat.user.profilePic}")
            val uriTask: Task<Uri> = storageReference.downloadUrl
            uriTask.addOnSuccessListener { uri1: Uri? ->
                Glide.with(context).load(uri1).into(holder.profilePic)
            }
            holder.itemView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in))
            holder.itemView.setOnClickListener {
                val intent = Intent(context, MessageActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                intent.putExtra("recipient", userChat.user.userId)
                intent.putExtra("phone", userChat.user.phoneNumber)
                context.startActivity(intent)
            }
            if(userChat.recipient!=userId) {
                if (userChat.isRead){
                    holder.read.visibility=View.VISIBLE
                    holder.sent.visibility=View.GONE
                }else{
                    holder.read.visibility=View.GONE
                    holder.sent.visibility=View.VISIBLE
                }
            }else{
                holder.read.visibility=View.GONE
                holder.sent.visibility=View.GONE
            }
        }else if(chat is ChannelItem){
            val channel=chat.channelChat
            holder.name.text = channel.channel.name
            holder.time.text = ConvertToDate.getDateAgo(channel.createdAt)
            holder.title.text = channel.title

            if (channel.channel.admin==userId){
                holder.read.visibility= View.VISIBLE
                holder.sent.visibility= View.GONE
            }else{
                holder.read.visibility= View.GONE
                holder.sent.visibility= View.GONE
            }
            holder.itemView.setOnClickListener {
                val intent = Intent(context, ChannelActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                intent.putExtra("id", channel.channel.id)
                intent.putExtra("name", channel.channel.name)
                intent.putExtra("admin", channel.channel.admin)
                intent.putExtra("icon", channel.channel.icon)
                intent.putStringArrayListExtra("members", ArrayList(channel.channel.members))
                context.startActivity(intent)
            }
            val storageReference = FirebaseStorage.getInstance()
                .getReference("images/${channel.channel.icon}")
            val uriTask: Task<Uri> = storageReference.downloadUrl
            uriTask.addOnSuccessListener { uri1: Uri? ->
                Glide.with(context).load(uri1).into(holder.profilePic)
            }

        }


    }
    @SuppressLint("NotifyDataSetChanged")
    private fun sortItems() {
        chats.sortBy { listItem ->
            when (listItem) {
                is ChatItem -> listItem.chat.createdAt
                is ChannelItem -> listItem.channelChat.createdAt
            }
        }
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return chats.size
    }
}