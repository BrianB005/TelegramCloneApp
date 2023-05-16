package com.brianbett.telegram
import android.annotation.SuppressLint
import android.content.Intent
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spanned
import android.util.Log
import android.widget.Toast
import com.brianbett.telegram.databinding.ActivityRegister2Binding
import com.brianbett.telegram.retrofit.AuthUser
import com.brianbett.telegram.retrofit.AuthUserInterface
import com.brianbett.telegram.retrofit.RetrofitHandler

class RegisterActivity2 : AppCompatActivity() {
    private lateinit var binding:ActivityRegister2Binding
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         binding=ActivityRegister2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        val codeInput=binding.inputCode

        val intent=intent
        val username=intent.getStringExtra("username")!!
        val phoneNumber=intent.getStringExtra("phoneNumber")!!
        val code=intent.getStringExtra("code")!!

        binding.submitBtn.setOnClickListener {
            val inputtedCode=codeInput.text.toString()
            if(inputtedCode.isEmpty()){
                binding.textInputCode.error="Kindly fill this field!"
            }else{

                if (code == inputtedCode){
                    val userDetails=HashMap<String,String>()
                    userDetails["username"]=username
                    userDetails["phoneNumber"]=phoneNumber
                    binding.submitBtn.text="Hold on ..."
                    RetrofitHandler.register(userDetails,object : AuthUserInterface {
                        override fun success(user: AuthUser) {
                            MyPreferences.saveItemToSP(applicationContext,"token",user.token)
                            MyPreferences.saveItemToSP(applicationContext,"userId",user.user.userId)
                            val intent2= Intent(applicationContext,MainActivity::class.java)
                            Toast.makeText(applicationContext,"Welcome!",Toast.LENGTH_SHORT).show()
                            startActivity(intent2)
                            finish()
                        }
                        @SuppressLint("SetTextI18n")
                        override fun failure(throwable: Throwable) {
                            Log.d("Exception",throwable.message!!)
                            binding.submitBtn.text="Submit"
                            binding.submitBtn.isEnabled=true
                        }
                        @SuppressLint("SetTextI18n")
                        override fun errorExists(message: String) {
                            Toast.makeText(applicationContext,message, Toast.LENGTH_LONG).show()
                            binding.submitBtn.text="Submit"
                            binding.submitBtn.isEnabled=true
                        }
                    } )
                }
            }
        }
    }






}
