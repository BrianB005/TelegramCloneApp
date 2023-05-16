package com.brianbett.telegram

import android.Manifest.permission.SEND_SMS
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.brianbett.telegram.databinding.ActivityRegister1Binding
import kotlin.random.Random

class RegisterActivity1 : AppCompatActivity() {
    private lateinit var  binding:ActivityRegister1Binding
    private lateinit var phone:String
    private lateinit var name:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityRegister1Binding.inflate(layoutInflater)
        setContentView(binding.root)
        val inputName=binding.inputName
        val inputNumber=binding.inputPhone

        binding.sendCodeBtn.setOnClickListener {
            name=inputName.text.toString()
            phone=inputNumber.text.toString()
            if(name.isEmpty()){
                binding.textInputUsername.error="Kindly fill this field!"
            }
            if(phone.isEmpty()){
                binding.textInputPhone.error="Kindly fill this field!"
            }
            if(name.isNotEmpty()&& phone.isNotEmpty()) {
                requestSmsPermission()

            }
        }

    }
    private fun generateCode(): String {
        val random = Random.Default
        val codeLength = 6

        val randomCode = random.nextInt(100_000,999_999)

        return String.format("%0${codeLength}d", randomCode)
    }
    private fun requestSmsPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                SEND_SMS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(SEND_SMS),
                123
            )
        } else {
            sendSms()
        }
    }
    private fun sendSms(){
        try {
            val smsManager = SmsManager.getDefault()

            val code=generateCode()
            val message="Use the verification code below to verify your identity\n $code"

            smsManager.sendTextMessage(phone, null, message, null, null)
            val intent=Intent(this@RegisterActivity1,RegisterActivity2::class.java)
            intent.putExtra("username",name)
            intent.putExtra("code",code)
            intent.putExtra("phoneNumber",phone)
            startActivity(intent)
            finish()
        } catch (e: Exception) {

            Log.d("SmsError", e.message.toString())

        }

    }


    // Handle permission request result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 130) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(applicationContext,"Permission granted! Press utto to continue",Toast.LENGTH_SHORT).show()
                sendSms()
            } else {
                Toast.makeText(applicationContext,"Denying the send SMS permission will make our app not work for you!",Toast.LENGTH_LONG).show()
            }
        }
    }

}
