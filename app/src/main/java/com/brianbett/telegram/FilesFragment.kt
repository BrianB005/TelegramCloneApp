package com.brianbett.telegram

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.brianbett.telegram.databinding.FragmentFilesBinding
import com.brianbett.telegram.databinding.FragmentVoiceBinding


class FilesFragment : Fragment(),SearchInterface {
    private lateinit var binding:FragmentFilesBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentFilesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun filterContent(query: String) {
        Log.d("Query .....",query)
    }


}