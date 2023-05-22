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
import com.brianbett.telegram.databinding.SingleSearchCResultBinding
import com.brianbett.telegram.retrofit.*
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage

class SearchResultsCRVAdapter(private var results:MutableList<SearchListItem<*>>, val context:Context) :RecyclerView.Adapter<SearchResultsCRVAdapter.MyViewHolder>(){

    class MyViewHolder(binding: SingleSearchCResultBinding):RecyclerView.ViewHolder(binding.root){
        val name=binding.phoneNumber
        val profilePic=binding.profilePic
        val onlineStatus=binding.onlineStatus
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val   searchCResultBinding=SingleSearchCResultBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(searchCResultBinding)

    }



    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val result = results[position]
        if(result is UserResult) {
            holder.name.text = result.userSearchResult.username
            val onlineStatus=if(result.userSearchResult.online)"Online" else "Last seen ${ConvertToDate.getDateAgoFromDate(result.userSearchResult.lastSeen)} " +
                    ConvertToDate.getTimeFromDate(result.userSearchResult.lastSeen)
            holder.onlineStatus.text=onlineStatus
            val storageReference = FirebaseStorage.getInstance()
                .getReference("images/${result.userSearchResult.profilePic}")
            val uriTask: Task<Uri> = storageReference.downloadUrl
            uriTask.addOnSuccessListener { uri1: Uri? ->
                Glide.with(context).load(uri1).into(holder.profilePic)
            }
            holder.itemView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in))
            holder.itemView.setOnClickListener {
                val intent = Intent(context, MessageActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                intent.putExtra("recipient", result.userSearchResult.userId)
                intent.putExtra("phone", result.userSearchResult.phoneNumber)
                context.startActivity(intent)
            }

        }else if(result is ChannelResult){
            val channel=result.channelDetails
            holder.name.text = channel.name
            val members=if(channel.members.size<2) "member" else "members"
            holder.onlineStatus.text= "${channel.members.size } $members"
            holder.itemView.setOnClickListener {
                val intent = Intent(context, ChannelActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                intent.putExtra("id", channel.id)
                intent.putExtra("name", channel.name)
                intent.putExtra("admin", channel.admin)
                intent.putExtra("icon", channel.icon)
                intent.putStringArrayListExtra("members", ArrayList(channel.members))
                context.startActivity(intent)
            }
            val storageReference = FirebaseStorage.getInstance()
                .getReference("images/${channel.icon}")
            val uriTask: Task<Uri> = storageReference.downloadUrl
            uriTask.addOnSuccessListener { uri1: Uri? ->
                Glide.with(context).load(uri1).into(holder.profilePic)
            }

        }


    }


    override fun getItemCount(): Int {
        return results.size
    }
}