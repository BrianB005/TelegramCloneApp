package com.brianbett.telegram

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.brianbett.telegram.databinding.FragmentMediaBinding
import com.brianbett.telegram.databinding.FragmentVoiceBinding


class MediaFragment : Fragment(),SearchInterface {
    private lateinit var binding:FragmentMediaBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentMediaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun filterContent(query: String) {

    }


}