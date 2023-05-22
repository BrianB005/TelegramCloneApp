package com.brianbett.telegram.adapters

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.brianbett.telegram.ConvertToDate
import com.brianbett.telegram.MyPreferences
import com.brianbett.telegram.R
import com.brianbett.telegram.retrofit.ChannelMessage
import com.brianbett.telegram.retrofit.Message
import java.util.*
import kotlin.collections.ArrayList

class ChannelMessagesRVAdapter(private val context: Context, private val messages:ArrayList<ChannelMessage>) :RecyclerView.Adapter<ChannelMessagesRVAdapter.MyViewHolder>() {
    private var isDifferentDay=false
    class MyViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val dateWrapper:CardView=view.findViewById(R.id.date_wrapper)
        val date:TextView=view.findViewById(R.id.date)
        val title: TextView =view.findViewById(R.id.title)
        val time: TextView =view.findViewById(R.id.time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return when(viewType){
            0->MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.outgoing_msg,parent,false))
            1->MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.incoming_msg,parent,false))
            else ->throw IllegalArgumentException("Invalid view type!")
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val message=messages[position]
        holder.time.text=ConvertToDate.getTime(message.createdAt)
        holder.title.text=message.title
        val currentDay=ConvertToDate.convertToDate(message.createdAt)
        val previous=if(position ==0) ConvertToDate.convertToDate(messages[position].createdAt) else ConvertToDate.convertToDate(messages[position-1].createdAt)
        val cal1= Calendar.getInstance()
        val cal2=Calendar.getInstance()
        cal1.time=currentDay!!
        cal2.time=previous!!
        isDifferentDay = if(position==0){
            true

        }else cal1.get(Calendar.DAY_OF_MONTH)!=cal2.get(Calendar.DAY_OF_MONTH)

        if (isDifferentDay){
            holder.dateWrapper.visibility=View.VISIBLE
            holder.date.text=ConvertToDate.getDateAgo(message.createdAt)!!
        }



    }


    override fun getItemViewType(position: Int): Int {
        val userId = MyPreferences.getItemFromSP(context, "userId")
        return if (messages[position].channel.admin==(userId)) 0 else 1
    }
    override fun getItemCount(): Int {
        return messages.size
    }
}