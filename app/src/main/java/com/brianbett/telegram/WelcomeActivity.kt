package com.brianbett.telegram

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatDelegate

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
//        val darkMode=MyPreferences.getItemFromSP(applicationContext,"dark")
//        if(darkMode!=null && darkMode=="yes"){
//            setTheme(R.style.Theme_Telegram)
//        }else{
//            setTheme(R.style.Theme_Telegram_LIGHT)
//        }
        setContentView(R.layout.activity_welcome)
        val token=MyPreferences.getItemFromSP(applicationContext,"token")!!


        Handler().postDelayed({
            if(token.isEmpty()){
                startActivity(Intent(applicationContext,RegisterActivity1::class.java))
            }
            else{
                startActivity(Intent(applicationContext,MainActivity::class.java))
            }
            finish()
        },2000)
    }
}