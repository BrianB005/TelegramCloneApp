package com.brianbett.telegram



import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.ViewGroup.LayoutParams
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
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
import com.brianbett.telegram.databinding.CreateChannelPopupBinding
import com.brianbett.telegram.retrofit.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import io.socket.client.Socket
import okhttp3.ResponseBody
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var adapter: ChatsRVAdapter;
    private lateinit var chatsList:ArrayList<Chat>


    private lateinit var newChat: FloatingActionButton
    private lateinit var noChats:TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar

    private lateinit var lightSwitch:AppCompatImageView
    private lateinit var darkSwitch:AppCompatImageView
//    combined list to hold both chats and channels
    private lateinit var combinedList:ArrayList<ListItem<*>>
    // custom onClickListener
    @SuppressLint("SetTextI18n")
    private fun myListener(viewId: Int, view:View){
        when(viewId){
            R.id.settings->{
                startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
                drawerLayout.closeDrawer(GravityCompat.START)
            }
            R.id.new_group->{
                val popupBinding=CreateChannelPopupBinding.inflate(layoutInflater)
                val popupWindow=PopupWindow(popupBinding.root,LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT,true)
                val background = View(applicationContext)

                background.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.black))
                background.alpha=0.8f
                val layoutParams = LinearLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT
                )
                binding.root.addView(background, layoutParams)
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
                popupWindow.setOnDismissListener {
                    binding.root.removeViewAt(binding.root.childCount - 1)
                }
                drawerLayout.closeDrawer(GravityCompat.START)
                popupBinding.closePopup.setOnClickListener {
                    popupWindow.dismiss()
                }
                popupBinding.createBtn.setOnClickListener {
                    val name=popupBinding.inputName.text.toString()
                    val bio=popupBinding.inputBio.text.toString()

                    if(name.isEmpty()){
                        popupBinding.nameInputWrapper.error="Kindly fill this field!"
                    }
                    if(bio.isEmpty()){
                        popupBinding.bioInputWrapper.error="Kindly fill this field!"
                    }

                    if(bio.isNotEmpty() && name.isNotEmpty()){
                        val channelDetails=HashMap<String,String>()

                        channelDetails["name"]=name
                        channelDetails["bio"]=bio

                        popupBinding.createBtn.text="Creating ..."
                        RetrofitHandler.createChannel(applicationContext,channelDetails,
                            object : ResponseBodyInterface {
                                override fun success(response: ResponseBody) {
                                    Toast.makeText(applicationContext,"$name Created successfully!",Toast.LENGTH_LONG).show()
                                    popupWindow.dismiss()
                                    popupBinding.createBtn.text="Create"
                                }

                                override fun failure(throwable: Throwable) {
                                    popupBinding.createBtn.text="Create"
                                    Toast.makeText(applicationContext,throwable.message,Toast.LENGTH_LONG).show()
                                }

                                override fun errorExists(message: String) {
                                    popupBinding.createBtn.text="Create"
                                    Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
                                }
                            })
                    }

                }

            }

        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val darkMode=MyPreferences.getItemFromSP(applicationContext,"dark")
//        if(darkMode!=null && darkMode=="yes"){
//            setTheme(R.style.Theme_Telegram)
//        }else{
//            setTheme(R.style.Theme_Telegram_LIGHT)
//        }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userId=MyPreferences.getItemFromSP(applicationContext,"userId")!!
        SocketIO.socket.connect()
        SocketIO.socket.on(Socket.EVENT_CONNECT) {
            Log.d("IOConnected", "Connected to IO Server")

            SocketIO.socket.emit("userConnected",userId)
        }
        setSupportActionBar(binding.appBarMain.toolbar)
         drawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val actionBarDrawerToggle=ActionBarDrawerToggle(this@MainActivity,drawerLayout,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val headerView=navView.getHeaderView(0)
        lightSwitch= headerView.findViewById(R.id.switch_to_light)
        darkSwitch= headerView.findViewById(R.id.switch_to_dark)
        if(darkMode=="yes"){
            lightSwitch.visibility=View.VISIBLE
            darkSwitch.visibility=View.GONE
        }else{
            darkSwitch.visibility=View.VISIBLE
            lightSwitch.visibility=View.GONE
        }
        darkSwitch.setOnClickListener {
//            setDark()

            darkSwitch.visibility=View.GONE
            lightSwitch.visibility=View.VISIBLE
            recreate()
        }
        lightSwitch.setOnClickListener {
//            setLight()

            lightSwitch.visibility=View.GONE
            darkSwitch.visibility=View.VISIBLE
            recreate()
        }
        navView.menu.forEach {menuItem->
            run {
                menuItem.setOnMenuItemClickListener {
                    myListener(menuItem.itemId,binding.root)
                    return@setOnMenuItemClickListener true
                }
            }
        }

        combinedList= ArrayList()
        adapter= ChatsRVAdapter(combinedList,applicationContext)
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

                    synchronized(combinedList) {
                        combinedList.addAll(chats.map { ChatItem(it) })

                    }
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


        RetrofitHandler.getMyChannelsPosts(applicationContext,object :ChannelChatsInterface{
            @SuppressLint("NotifyDataSetChanged")
            override fun success(posts: List<ChannelChat>) {
                synchronized(combinedList) {
                    combinedList.addAll(posts.map { ChannelItem(it) })
                }

                adapter.notifyDataSetChanged()
            }

            override fun failure(throwable: Throwable) {
                Toast.makeText(applicationContext,throwable.message,Toast.LENGTH_LONG).show()
            }

            override fun errorExists(message: String) {
                Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
            }

        })
        SocketIO.socket.on("messageReceived"){ args ->
            runOnUiThread {
                val response = args[0] as JSONObject
                val title=response.getString("title")
                val senderObject=response.getJSONObject("sender")
                val recipientObject=response.getJSONObject("recipient")
                val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault())

                val sender=UserDetails(senderObject.getString("_id"),
                    senderObject.getString("username"),dateFormat.parse(senderObject.getString("lastSeen"))!!,
                    senderObject.getBoolean("online"),senderObject.getString("phoneNumber"),senderObject.getString("profilePic"))
                val recipient=UserDetails(recipientObject.getString("_id"),
                    recipientObject.getString("username"),dateFormat.parse(recipientObject.getString("lastSeen"))!!,
                    recipientObject.getBoolean("online"),recipientObject.getString("phoneNumber"),recipientObject.getString("profilePic"))
                val createdAt=response.getString("createdAt")


                val user=if(sender.userId==userId) recipient else sender
                val chat=Chat(user.userId,user,title,createdAt,response.getBoolean("isRead"),recipient.userId)
                val chatItem=ChatItem(chat)

//                checking if item already exists that we update it
                val itemIndex = combinedList.indexOfFirst { listItem ->
                    if (listItem is ChatItem) {
                        listItem.chat.user == chatItem.chat.user
                    } else {
                        false
                    }
                }

                if(itemIndex!=-1){
                    combinedList[itemIndex]=chatItem
                    adapter.notifyItemChanged(itemIndex)
                }else{
                    combinedList.add(0,chatItem)
                    adapter.notifyItemInserted(0)
                }
            }
        }
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
                val post=ChannelChat(id,channel,title,createdAt )
                val channelItem=ChannelItem(post)
                // checking if item already exists that we update it
                val itemIndex = combinedList.indexOfFirst { listItem ->
                    if (listItem is ChannelItem) {
                        listItem.channelChat.channel == channelItem.channelChat.channel
                    } else {
                        false
                    }
                }

                if(itemIndex!=-1){
                    combinedList[itemIndex]=channelItem
                    adapter.notifyItemChanged(itemIndex)
                }else{
                    combinedList.add(0,channelItem)
                    adapter.notifyItemInserted(0)
                }
            }
        }



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
            R.id.search->{
                startActivity(Intent(applicationContext,SearchActivity::class.java))
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.home_toolbar_menu, menu)
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        SocketIO.socket.disconnect()
    }

    private fun setDark() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        MyPreferences.saveItemToSP(applicationContext,"dark","yes")
        recreate()
    }

    private fun setLight() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        MyPreferences.saveItemToSP(applicationContext,"dark","no")
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