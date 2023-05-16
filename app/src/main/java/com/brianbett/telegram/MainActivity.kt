package com.brianbett.telegram



import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.core.view.forEach
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.brianbett.telegram.adapters.ChatsRVAdapter

import com.brianbett.telegram.adapters.MessagesRVAdapter
import com.brianbett.telegram.databinding.ActivityMainBinding
import com.brianbett.telegram.retrofit.Chat
import com.brianbett.telegram.retrofit.ChatsInterface
import com.brianbett.telegram.retrofit.RetrofitHandler
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import io.socket.client.Socket


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var adapter: ChatsRVAdapter;
    private lateinit var chatsList:ArrayList<Chat>


    private lateinit var newChat: FloatingActionButton
    private lateinit var noChats:TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    // custom onClickListener
    private fun myListener(viewId: Int){
        when(viewId){
            R.id.settings->{
                startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
            }
            R.id.new_group->{
                switchTheme()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        SocketIO.socket.connect()
        SocketIO.socket.on(Socket.EVENT_CONNECT) {
            Log.d("IOConnected", "Connected to IO Server")
            val userId=MyPreferences.getItemFromSP(applicationContext,"userId")!!
            SocketIO.socket.emit("userConnected",userId)
        }
        setSupportActionBar(binding.appBarMain.toolbar)
         drawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val actionBarDrawerToggle=ActionBarDrawerToggle(this@MainActivity,drawerLayout,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        navView.menu.forEach {menuItem->
            run {
                menuItem.setOnMenuItemClickListener {
                    myListener(menuItem.itemId)
                    return@setOnMenuItemClickListener true
                }
            }
        }


        chatsList=ArrayList()
        adapter= ChatsRVAdapter(chatsList,applicationContext)
        recyclerView=binding.appBarMain.recyclerView
        progressBar=binding.appBarMain.progress
        newChat=binding.appBarMain.newChat
        noChats=binding.appBarMain.noChats

        recyclerView.adapter=adapter
        recyclerView.layoutManager=LinearLayoutManager(applicationContext)


        binding.appBarMain.newChat.setOnClickListener {
            startActivity(Intent(applicationContext,UsersActivity::class.java))
        }


        progressBar.visibility=View.VISIBLE

        RetrofitHandler.getChats(applicationContext,object:ChatsInterface{
            @SuppressLint("NotifyDataSetChanged")
            override fun success(chats: List<Chat>) {
                progressBar.visibility=View.GONE
                if(chats.isEmpty()){
                    recyclerView.visibility=View.GONE
                    noChats.visibility=View.VISIBLE

                }else{
                    recyclerView.visibility=View.VISIBLE
                    chatsList.addAll(chats)
                    adapter.notifyDataSetChanged()
                    noChats.visibility=View.GONE

                }

            }

            override fun failure(throwable: Throwable) {
                progressBar.visibility=View.GONE
                Toast.makeText(applicationContext,throwable.message,Toast.LENGTH_LONG).show()
            }

            override fun errorExists(message: String) {
                progressBar.visibility=View.GONE
                Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
            }

        })



    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    drawerLayout.openDrawer(GravityCompat.START)
                }
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
    private fun toggleTheme() {
        val currentTheme = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        val isDarkTheme = currentTheme == Configuration.UI_MODE_NIGHT_YES

        if (isDarkTheme) {
            setTheme(R.style.Theme_Telegram)
        } else {
            setTheme(R.style.Theme_Telegram_LIGHT)
        }
        recreate()
    }
    private fun switchTheme() {
        val newNightMode = when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> AppCompatDelegate.MODE_NIGHT_YES
            Configuration.UI_MODE_NIGHT_YES -> AppCompatDelegate.MODE_NIGHT_NO
            else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }
        AppCompatDelegate.setDefaultNightMode(newNightMode)
        recreate()
    }



}