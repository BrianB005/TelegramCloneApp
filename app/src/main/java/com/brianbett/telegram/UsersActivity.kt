package com.brianbett.telegram

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brianbett.telegram.adapters.UsersRVAdapter
import com.brianbett.telegram.databinding.ActivityUsersBinding
import com.brianbett.telegram.retrofit.*

class UsersActivity : AppCompatActivity() {
    private lateinit var binding:ActivityUsersBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var usersList:ArrayList<UserDetails>
    private lateinit var adapter:UsersRVAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView=binding.recyclerView
        progressBar=binding.progress
        usersList= ArrayList()
        adapter= UsersRVAdapter(usersList,applicationContext)

        recyclerView.adapter=adapter
        recyclerView.layoutManager=LinearLayoutManager(applicationContext)

        progressBar.visibility= View.VISIBLE
        recyclerView.visibility=View.VISIBLE

        RetrofitHandler.getUsers(applicationContext,object: UsersInterface {
            @SuppressLint("NotifyDataSetChanged")
            override fun success(users: List<UserDetails>) {
                progressBar.visibility=View.GONE

                if(users.isNotEmpty()){
                    recyclerView.visibility=View.VISIBLE
                    usersList.addAll(users)
                    adapter.notifyDataSetChanged()
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
    }
}