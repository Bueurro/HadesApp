package com.example.hadesapp.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hadesapp.databinding.FragmentPersonajeAddBinding

class PersonajeAddFragment : Fragment() {
    private lateinit var binding: FragmentPersonajeAddBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View {
        binding = FragmentPersonajeAddBinding.inflate(inflater, container, false)



        return binding.root
    }

}