package com.brianbett.telegram

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.brianbett.telegram.databinding.ActivitySettingsBinding
import com.google.android.material.appbar.AppBarLayout
import java.lang.Math.abs

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.collapsingToolbar.title = ""
        binding.appBarLayout.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (kotlin.math.abs(verticalOffset) == appBarLayout.totalScrollRange) {
                binding.toolbar.title = "Username"
                binding.toolbar.subtitle = "Online"
                binding.username.visibility= View.GONE
                binding.onlineStatus.visibility= View.GONE
                binding.profilePicWrapper.visibility=View.GONE
                binding.toolbar.logo = ContextCompat.getDrawable(this, R.drawable.image3)
            } else {
                binding.toolbar.title = ""
                binding.toolbar.subtitle = ""
                binding.username.visibility= View.VISIBLE
                binding.onlineStatus.visibility= View.VISIBLE
                binding.profilePicWrapper.visibility=View.VISIBLE
//                binding.profilePic.setImageBitmap( R.drawable.image3)
                binding.toolbar.logo = null
            }
        }
    }
}