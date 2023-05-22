package com.brianbett.telegram.adapters


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.brianbett.telegram.ConvertToDate
import com.brianbett.telegram.MessageActivity
import com.brianbett.telegram.MyPreferences

import com.brianbett.telegram.databinding.SingleUserBinding
import com.brianbett.telegram.retrofit.UserDetails
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage


class UsersRVAdapter(private val users:List<UserDetails>, val context:Context) :RecyclerView.Adapter<UsersRVAdapter.MyViewHolder>(){

    class MyViewHolder(binding: SingleUserBinding):RecyclerView.ViewHolder(binding.root){
        val name=binding.username
        val profilePic=binding.profilePic
        val phoneNumber=binding.phoneNumber
        val status=binding.onlineStatus
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val   userBinding=SingleUserBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(userBinding)

    }



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = users[position]
        val onlineStatus=if(user.online)"Online" else "Last seen ${ConvertToDate.getDateAgoFromDate(user.lastSeen)} " +
                ConvertToDate.getTimeFromDate(user.lastSeen)
        holder.name.text=user.username
        holder.phoneNumber.text=user.phoneNumber

        holder.status.text=onlineStatus

        holder.itemView.setOnClickListener {
            val intent=Intent(context,MessageActivity::class.java)
            intent.flags=Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra("recipient",user.userId)
            intent.putExtra("phone",user.phoneNumber)
            context.startActivity(intent)
        }
        val storageReference = FirebaseStorage.getInstance()
            .getReference("images/${user.profilePic}")
        val uriTask: Task<Uri> = storageReference.downloadUrl
        uriTask.addOnSuccessListener { uri1: Uri? ->
            Glide.with(context).load(uri1).into(holder.profilePic)
        }


    }

    override fun getItemCount(): Int {
        return users.size
    }
}