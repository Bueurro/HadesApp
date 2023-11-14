package com.example.hadesapp.views.fragments

import android.content.Intent
import com.example.hadesapp.R
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.hadesapp.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)

        binding.btnPersonajes.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_personajesFragment)
        }

        binding.btnBendiciones.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_bendicionesFragment)
        }

        binding.btnArmas.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_armasFragment2)
        }


        return binding.root
    }

}