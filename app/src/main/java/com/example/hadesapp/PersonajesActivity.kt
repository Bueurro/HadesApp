package com.example.hadesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hadesapp.databinding.ActivityPersonajesBinding

class PersonajesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPersonajesBinding
    private lateinit var personajeAdapter: PersonajeAdapter
    private lateinit var linearLayoutManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonajesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // INICIALIZAR

        personajeAdapter = PersonajeAdapter(mutableListOf())
        linearLayoutManager = LinearLayoutManager(this)

        // RECYCLEVIEW
        binding.rvPersonajes.apply {
            layoutManager = linearLayoutManager
            adapter = personajeAdapter
        }

    }
}