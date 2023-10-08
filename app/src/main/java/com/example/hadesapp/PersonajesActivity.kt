package com.example.hadesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hadesapp.databinding.ActivityPersonajesBinding

class PersonajesActivity : AppCompatActivity(), OnClickListener{
    override fun onClick(personaje: Personaje, position: Int) {
        Toast.makeText(this, "$position: ${personaje.toString()}", Toast.LENGTH_SHORT ).show()
    }

    private lateinit var binding: ActivityPersonajesBinding
    private lateinit var personajeAdapter: PersonajeAdapter
    private lateinit var linearLayoutManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonajesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // INICIALIZAR

        personajeAdapter = PersonajeAdapter(getPersonaje(),this)
        linearLayoutManager = LinearLayoutManager(this)

        // RECYCLEVIEW
        binding.rvPersonajes.apply {
            layoutManager = linearLayoutManager
            adapter = personajeAdapter
        }

        binding.imgBtnVolver.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }

    private fun getPersonaje(): MutableList<Personaje> {
        val personajes = mutableListOf<Personaje>()

        val zagreo = Personaje(1,"Zagreo","Dios Ctónico","Dios de la sangre","Ninguno","https://static.wikia.nocookie.net/hades_gamepedia_en/images/2/29/Zagreus.png/revision/latest/scale-to-width-down/200?cb=20181210044005");
        val afrodita = Personaje(1,"Afrodita","Dios Del Olimpo","Diosa del amor","rosa","https://static.wikia.nocookie.net/hades_gamepedia_en/images/c/c6/Aphrodite.png/revision/latest/scale-to-width-down/350?cb=20200707184521");

        personajes.add(zagreo)
        personajes.add(afrodita)
        personajes.add(zagreo)
        personajes.add(afrodita)
        personajes.add(zagreo)
        personajes.add(afrodita)
        personajes.add(zagreo)
        personajes.add(afrodita)
        personajes.add(zagreo)
        personajes.add(afrodita)

        return personajes
    }

}