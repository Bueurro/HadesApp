package com.example.hadesapp.views.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hadesapp.databinding.FragmentMainBinding
import com.example.hadesapp.views.ArmasActivity
import com.example.hadesapp.views.BendicionesActivity
import com.example.hadesapp.views.PersonajesActivity

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)

        binding.btnPersonajes.setOnClickListener {
            val intent = Intent(activity, PersonajesActivity::class.java)
            startActivity(intent)
        }

        binding.btnBendiciones.setOnClickListener {
            val intent = Intent(activity, BendicionesActivity::class.java)
            startActivity(intent)
        }

        binding.btnArmas.setOnClickListener {
            val intent = Intent(activity, ArmasActivity::class.java)
            startActivity(intent)
        }



        return binding.root
    }

}