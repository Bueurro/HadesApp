package com.example.hadesapp.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hadesapp.R
import com.example.hadesapp.databinding.FragmentAspectoAddBinding

class AspectoAddFragment : Fragment() {
    private lateinit var binding : FragmentAspectoAddBinding
    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        binding = FragmentAspectoAddBinding.inflate(inflater, container, false)



        return binding.root
    }

}