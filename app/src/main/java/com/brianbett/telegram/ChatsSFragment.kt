package com.brianbett.telegram

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import com.brianbett.telegram.adapters.SearchResultsCRVAdapter
import com.brianbett.telegram.databinding.FragmentChatsSBinding
import com.brianbett.telegram.databinding.FragmentVoiceBinding
import com.brianbett.telegram.retrofit.*


class ChatsSFragment : Fragment(),SearchInterface {
    private lateinit var binding:FragmentChatsSBinding
    private lateinit var combinedList:ArrayList<SearchListItem<*>>
    private lateinit var adapter:SearchResultsCRVAdapter
    private lateinit var progressBar: ProgressBar
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentChatsSBinding.inflate(inflater, container, false)

        combinedList= ArrayList()
        adapter= SearchResultsCRVAdapter(combinedList,requireContext())
        val recyclerView=binding.recyclerView
        progressBar=binding.progress
        recyclerView.adapter=adapter
        recyclerView.layoutManager=LinearLayoutManager(requireContext())

        return binding.root
    }
    override fun filterContent(query: String) {
        progressBar.visibility=View.VISIBLE
        combinedList.clear()
        if(query.isNotEmpty()) {
            RetrofitHandler.searchChannels(
                requireContext(),
                query,
                object : ChannelSearchInterface {
                    @SuppressLint("NotifyDataSetChanged")
                    override fun success(channels: List<ChannelDetails>) {
                        synchronized(combinedList) {
                            combinedList.addAll(channels.map { ChannelResult(it) })
                            adapter.notifyDataSetChanged()
                        }
                        progressBar.visibility = View.GONE
                    }

                    override fun failure(throwable: Throwable) {
                        Log.e("Search Exception", throwable.message!!)
                        progressBar.visibility = View.GONE
                    }

                    override fun errorExists(message: String) {
                        Log.e("Search Exception", message)
                        progressBar.visibility = View.GONE
                    }

                })
            RetrofitHandler.searchUsers(requireContext(), query, object : UserSearchInterface {
                @SuppressLint("NotifyDataSetChanged")
                override fun success(users: List<UserDetails>) {
                    progressBar.visibility = View.GONE
                    synchronized(combinedList) {
                        combinedList.addAll(users.map { UserResult(it) })
                        adapter.notifyDataSetChanged()
                    }
                }
                override fun failure(throwable: Throwable) {
                    Log.e("Search Exception", throwable.message!!)
                    progressBar.visibility = View.GONE
                }

                override fun errorExists(message: String) {
                    Log.e("Search Exception", message)
                    progressBar.visibility = View.GONE
                }

            })
        }
    }


}