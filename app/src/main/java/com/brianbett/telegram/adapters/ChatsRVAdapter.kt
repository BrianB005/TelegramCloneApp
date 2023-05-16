package com.brianbett.telegram.adapters


import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.brianbett.telegram.ConvertToDate
import com.brianbett.telegram.MessageActivity
import com.brianbett.telegram.databinding.SingleChatBinding
import com.brianbett.telegram.retrofit.Chat

class ChatsRVAdapter(private val chats:List<Chat>, val context:Context) :RecyclerView.Adapter<ChatsRVAdapter.MyViewHolder>(){

    class MyViewHolder(binding: SingleChatBinding):RecyclerView.ViewHolder(binding.root){
        val name=binding.profileName
        val profilePic=binding.profilePic
        val title=binding.chatTitle
        val time=binding.time
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val   chatBinding=SingleChatBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(chatBinding)

    }



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val chat = chats[position]
        holder.name.text=chat.user.phoneNumber
        holder.time.text=ConvertToDate.getDateAgo(chat.createdAt)
        holder.name.text=chat.user.phoneNumber
        holder.title.text=chat.title

        holder.itemView.setOnClickListener {
            val intent=Intent(context,MessageActivity::class.java)
            intent.flags=Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra("recipient",chat.user.userId)
            intent.putExtra("phone",chat.user.phoneNumber)
            context.startActivity(intent)
        }


    }

    override fun getItemCount(): Int {
        return chats.size
    }
}